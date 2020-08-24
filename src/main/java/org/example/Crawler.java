package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Crawler {


    private static final Pattern libraryPattern = Pattern.compile("(([a-zA-Z0-9]|\\.|-|_)+)(\\.js)");
    private static final Pattern libraryName = Pattern.compile("([a-zA-Z0-9]|-)*");

    /*
    I was not sure if this supposed to be program running in some kind of
    while loop, but In my opinion such command line tools seems sufficient.
    You can provide search term from command line when executing program like:
        java -jar crawl-1.0-SNAPSHOT.jar some search term provided
    You can expect output like:
        Provided search term: some search term provided
        gtm was used 10 times in results pages
        jquery was used 10 times in results pages
        analytics was used 3 times in results pages
        skins was used 3 times in results pages
        cdn was used 3 times in results pages

     */
    public static void main(String[] args) {
        final String searchTerm = Arrays.stream(args)
                .collect(Collectors.joining(" "));
        System.out.println("Provided search term: " + searchTerm);
        final List<String> googleSearchResults = getGoogleSearchResults(searchTerm);
       /*
       I'm using simple solution with parallelStream as this is simple application with
       one simple goal and only one clear - IO http calls - candidate to improve with parallel execution.
        */
        final Map<String, Long> collect = googleSearchResults.parallelStream()
                .map(Crawler::parsePageToFindLibs)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        Function.identity(), Collectors.counting()));
        /*
        I did this as 2 step operation, because using Collectors.collectingAndThen seems to me as
        much less readable in this case.
         */
        collect.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(5)
                .forEach(entry ->
                        System.out.println(entry.getKey()
                                + " was used "
                                + entry.getValue()
                                + " times in results pages"));
    }

    /*
    From what I learned about google search for application use, You have to create
    Programmable Search Engine instance and obtain api-key for Your application.
    Since there is option to provide mock for some step I decided to mock
    google search and return just what I'm interested in - list of links
    to top search results.
    To be exact I'm returning first page of result for search term "web crawler" from my google account
    personalized results
    Response from google search is some json structure, and to transform it to list of strings
    with links to result pages I would try to pare it line by line if requirement about not using external libraries
    is really crucial, if not I would use something like Jackson and annotate model with
    ignoreUnknown to fetch only those parts of model that I need and then create list of links from that
     */
    public static List<String> getGoogleSearchResults(String searchTerm) {
        return Arrays.asList("https://pl.wikipedia.org/wiki/Robot_internetowy",
                "https://en.wikipedia.org/wiki/Web_crawler",
                "https://www.webcrawler.com/",
                "https://www.octoparse.com/blog/top-20-web-crawling-tools-for-extracting-web-data",
                "https://scrapy.org/",
                "https://www.google.com/search/howsearchworks/crawling-indexing/",
                "https://www.webfx.com/blog/internet/what-is-a-web-crawler/",
                "https://www.sciencedaily.com/terms/web_crawler.htm",
                "https://www.screamingfrog.co.uk/seo-spider/",
                "https://en.ryte.com/wiki/Crawler",
                "https://www.w3.org/wiki/Write_Web_Crawler");
    }

    /*
    This idea with regexp is first thing that came to my mind while looking at some random
    html page, but it is definitely neither complex parsing algorithm nor accurate solution.
    In this method there is plenty space for improvements.
    When testing on real sites I've found out that important information about library is often
    somewhere in link like in case of https://www.google-analytics.com/plugins/ua/linkid.js
    and https://www.google-analytics.com/analytics.js where just filename doesn't tell us much about
    origins of such library. I tried several times to advance complexity of this algorithm
    and take that fact into consideration, but at the end I didn't succeeded in providing
    clear improvements and finally decided to stay with this simple solution for now.
    This part of solution is definitely bothering me the most.
    */
    public static List<String> parsePageToFindLibs(String url) {
        List<String> result = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(UrlWrapper.getInputStream(url), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                final Matcher matcher = libraryPattern.matcher(line);
                if (matcher.find()) {
                    /*
                    This second matcher is just some simple optimization to count entries like:
                    jquery.js and jquery.min.js as just jquery,
                    (also possibly different release versions etc.)
                     */
                    final Matcher nameMatcher = libraryName.matcher(matcher.group(0));
                    if (nameMatcher.find()) {
                        result.add(nameMatcher.group(0));
                    }
                }
            }
        } catch (IOException e) {
            /*
            It is not clear to me what to do when e.g response status is 4XX or other exceptions are thrown
            while parsing result therefore I decided, that returning empty list in case of such problems
            is ok
             */
            e.printStackTrace();
            return Collections.emptyList();
        }
        return result;
    }


    /*
    This wrapper class and method are just for easy testing while working on solution
    Also if I had more time I would definitely use java 11 httpClient but since I don't
    have much experience with it I decided to use well known URL to fetch data.
    However if requirements about not using external libs are not so crucial I would at least
    do a little research for some well designed lib for this task, to take some inspiration at least.
     */
    static class UrlWrapper {
        public static InputStream getInputStream(String url) throws IOException {
            return new URL(url).openStream();
        }
    }
}
