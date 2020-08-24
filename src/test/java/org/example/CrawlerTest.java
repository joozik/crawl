package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;


/*
I didnt have time to think about some advanced testing, But if provided more time I would think
of refactoring code to be more testable.
I also decided to use testing framework as providing tests with some pure java methods
seems time consuming, error prone and not good idea in my opinion. However as I wanted to
use latest junit libs I encountered some issues with maven-surefire-plugin and wasted some
time on that.
 */
class CrawlerTest {

    @ParameterizedTest
    @MethodSource
    void parsePageToFindLibs(InputStream inputStream, List<String> expectedResultList) {
        try (MockedStatic<Crawler.UrlWrapper> urlWrapperMock = mockStatic(Crawler.UrlWrapper.class)) {
            urlWrapperMock.when((MockedStatic.Verification) Crawler.UrlWrapper.getInputStream(anyString())).thenReturn(inputStream);
            final List<String> actualResultList = Crawler.parsePageToFindLibs("anyURl");
            assertIterableEquals(expectedResultList, actualResultList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Stream<Arguments> parsePageToFindLibs() {
        return Stream.of(
                Arguments.of(new ByteArrayInputStream(ONE_LIBRARY.getBytes(StandardCharsets.UTF_8)),
                        Collections.singletonList("howler2")),
                Arguments.of(new ByteArrayInputStream(MULTIPLE_LIBRARIES.getBytes(StandardCharsets.UTF_8)),
                        Arrays.asList("jquery", "some-js-library", "some-other-js-library", "jquery")),
                Arguments.of(new ByteArrayInputStream((EXAMPLE_PAGE_HTML).getBytes(StandardCharsets.UTF_8)),
                        Arrays.asList("linkid",
                                "analytics",
                                "analytics",
                                "pub",
                                "xdLocalStorage",
                                "qevents",
                                "fbevents",
                                "insight",
                                "button",
                                "widgets"))
        );
    }


    private static final String ONE_LIBRARY = "<script src=\"howler2.min.js\"></script>\n";
    private static final String MULTIPLE_LIBRARIES = "<script src=\"jquery.min.js\"></script>\n" +
            "<script src=\"some-js-library.min.js\"></script>\n" +
            "<script src=\"some-other-js-library.min.js\"></script>\n" +
            "<script src=\"jquery.js\"></script>\n";

    private static final String EXAMPLE_PAGE_HTML ="<html><head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "\n" +
            "    <title>Scrapy | A Fast and Powerful Scraping and Web Crawling Framework</title>\n" +
            "    <meta name=\"description\" content=\"\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"57x57\" href=\"/favicons/apple-touch-icon-57x57.png\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"114x114\" href=\"/favicons/apple-touch-icon-114x114.png\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"72x72\" href=\"/favicons/apple-touch-icon-72x72.png\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"144x144\" href=\"/favicons/apple-touch-icon-144x144.png\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"60x60\" href=\"/favicons/apple-touch-icon-60x60.png\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"120x120\" href=\"/favicons/apple-touch-icon-120x120.png\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"76x76\" href=\"/favicons/apple-touch-icon-76x76.png\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"152x152\" href=\"/favicons/apple-touch-icon-152x152.png\">\n" +
            "\t<link rel=\"apple-touch-icon\" sizes=\"180x180\" href=\"/favicons/apple-touch-icon-180x180.png\">\n" +
            "\t<link rel=\"icon\" type=\"image/png\" href=\"/favicons/favicon-192x192.png\" sizes=\"192x192\">\n" +
            "\t<link rel=\"icon\" type=\"image/png\" href=\"/favicons/favicon-160x160.png\" sizes=\"160x160\">\n" +
            "\t<link rel=\"icon\" type=\"image/png\" href=\"/favicons/favicon-96x96.png\" sizes=\"96x96\">\n" +
            "\t<link rel=\"icon\" type=\"image/png\" href=\"/favicons/favicon-16x16.png\" sizes=\"16x16\">\n" +
            "\t<link rel=\"icon\" type=\"image/png\" href=\"/favicons/favicon-32x32.png\" sizes=\"32x32\">\n" +
            "\t<meta name=\"msapplication-TileColor\" content=\"#da532c\">\n" +
            "\t<meta name=\"msapplication-TileImage\" content=\"/favicons/mstile-144x144.png\">\n" +
            "    <meta name=\"viewport\" content=\"width=980\">\n" +
            "\n" +
            "    <link rel=\"stylesheet\" href=\"/css/main.css\">\n" +
            "    <link rel=\"stylesheet\" href=\"/css/font-awesome.min.css\">\n" +
            "\n" +
            "    <link href=\"https://fonts.googleapis.com/css?family=Bitter:400,700,400italic\" rel=\"stylesheet\" type=\"text/css\">\n" +
            "\t<link href=\"https://fonts.googleapis.com/css?family=Ubuntu+Mono\" rel=\"stylesheet\" type=\"text/css\">\n" +
            "    <link href=\"https://fonts.googleapis.com/css?family=Ubuntu\" rel=\"stylesheet\" type=\"text/css\">\n" +
            "    <link href=\"https://fonts.googleapis.com/css?family=Bitter\" rel=\"stylesheet\" type=\"text/css\">\n" +
            "\t<script type=\"text/javascript\" async=\"\" src=\"https://www.google-analytics.com/plugins/ua/linkid.js\">" +
            "</script><script type=\"text/javascript\" async=\"\" src=\"https://www.google-analytics.com/analytics.js\"></script>" +
            "<script type=\"text/javascript\" async=\"\" src=\"https://www.googleadservices.com/pagead/conversion_async.js\"></script>" +
            "<script id=\"twitter-wjs\" src=\"https://platform.twitter.com/widgets.js\"></script>" +
            "<script type=\"text/javascript\" async=\"\" src=\"https://snap.licdn.com/li.lms-analytics/insight.min.js\">" +
            "</script><script src=\"https://connect.facebook.net/signals/config/1087285701470339?v=2.9.23&amp;r=stable\" async=\"\"></script>" +
            "<script async=\"\" src=\"https://connect.facebook.net/en_US/fbevents.js\"></script>" +
            "<script async=\"\" src=\"https://a.quora.com/qevents.js\"></script>" +
            "<script type=\"text/javascript\" async=\"\" src=\"https://cdn.segment.com/analytics.js/v1/8UDQfnf3cyFSTsM4YANnW5sXmgZVILbA/analytics.min.js\"></script>" +
            "<script async=\"\" src=\"https://www.google-analytics.com/analytics.js\"></script>" +
            "<script>\n" +
            "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
            "(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
            "m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
            "})(window,document,'script','https://www.google-analytics.com/analytics.js','gaScrapy');\n" +
            "\n" +
            "gaScrapy('create', 'UA-10231918-1', 'auto');\n" +
            "gaScrapy('send', 'pageview');\n" +
            "</script>\n" +
            "\n" +
            "<script type=\"text/javascript\">\n" +
            "!function(){var analytics=window.analytics=window.analytics||[];if(!analytics.initialize)if(analytics.invoked)window.console&&console.error&&console.error(\"Segment snippet included twice.\");else{analytics.invoked=!0;analytics.methods=[\"trackSubmit\",\"trackClick\",\"trackLink\",\"trackForm\",\"pageview\",\"identify\",\"reset\",\"group\",\"track\",\"ready\",\"alias\",\"page\",\"once\",\"off\",\"on\"];analytics.factory=function(t){return function(){var e=Array.prototype.slice.call(arguments);e.unshift(t);analytics.push(e);return analytics}};for(var t=0;t<analytics.methods.length;t++){var e=analytics.methods[t];analytics[e]=analytics.factory(e)}analytics.load=function(t){var e=document.createElement(\"script\");e.type=\"text/javascript\";e.async=!0;e.src=(\"https:\"===document.location.protocol?\"https://\":\"http://\")+\"cdn.segment.com/analytics.js/v1/\"+t+\"/analytics.min.js\";var n=document.getElementsByTagName(\"script\")[0];n.parentNode.insertBefore(e,n)};analytics.SNIPPET_VERSION=\"3.1.0\";\n" +
            "analytics.load(\"8UDQfnf3cyFSTsM4YANnW5sXmgZVILbA\");\n" +
            "analytics.page();\n" +
            "}}();\n" +
            "\n" +
            "analytics.ready(function () {\n" +
            "    ga('require', 'linker');\n" +
            "    ga('linker:autoLink', ['scrapinghub.com', 'crawlera.com']);\n" +
            "});\n" +
            "</script>\n" +
            "\n" +
            "<!-- REFERSION TRACKING: BEGIN -->\n" +
            "<script src=\"//scrapinghub.refersion.com/tracker/v3/pub_187fec375fa525a197a6.js\"></script>\n" +
            "<script>_refersion();</script><script type=\"text/javascript\" src=\"https://scrapinghub.refersion.com/js/xdLocalStorage.min.js?v=94\"></script>\n" +
            "<!-- REFERSION TRACKING: END -->\n" +
            "\n" +
            "<!-- Google Ads: 973726542 Re-targeting code-->\n" +
            "<script async=\"\" src=\"https://www.googletagmanager.com/gtag/js?id=AW-973726542\"></script>\n" +
            "<script>\n" +
            "  window.dataLayer = window.dataLayer || [];\n" +
            "  function gtag(){dataLayer.push(arguments);}\n" +
            "  gtag('js', new Date());\n" +
            "\n" +
            "  gtag('config', 'AW-973726542');\n" +
            "</script>\n" +
            "\n" +
            "<!-- Quora Pixel Code (JS Helper) -->\n" +
            "<script>\n" +
            "!function(q,e,v,n,t,s){if(q.qp) return; n=q.qp=function(){n.qp?n.qp.apply(n,arguments):n.queue.push(arguments);}; n.queue=[];t=document.createElement(e);t.async=!0;t.src=v; s=document.getElementsByTagName(e)[0]; s.parentNode.insertBefore(t,s);}(window, 'script', 'https://a.quora.com/qevents.js');\n" +
            "qp('init', 'd3cdc58cc507450a97a918ffd749d99a');\n" +
            "qp('track', 'ViewContent');\n" +
            "</script>\n" +
            "<noscript><img height=\"1\" width=\"1\" style=\"display:none\" src=\"https://q.quora.com/_/ad/d3cdc58cc507450a97a918ffd749d99a/pixel?tag=ViewContent&noscript=1\"/></noscript>\n" +
            "<!-- End of Quora Pixel Code -->\n" +
            "\n" +
            "<!-- Facebook Pixel Code -->\n" +
            "<script>\n" +
            "  !function(f,b,e,v,n,t,s)\n" +
            "  {if(f.fbq)return;n=f.fbq=function(){n.callMethod?\n" +
            "  n.callMethod.apply(n,arguments):n.queue.push(arguments)};\n" +
            "  if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';\n" +
            "  n.queue=[];t=b.createElement(e);t.async=!0;\n" +
            "  t.src=v;s=b.getElementsByTagName(e)[0];\n" +
            "  s.parentNode.insertBefore(t,s)}(window, document,'script',\n" +
            "  'https://connect.facebook.net/en_US/fbevents.js');\n" +
            "  fbq('init', '1087285701470339');\n" +
            "  fbq('track', 'PageView');\n" +
            "</script>\n" +
            "<noscript><img height=\"1\" width=\"1\" style=\"display:none\"\n" +
            "  src=\"https://www.facebook.com/tr?id=1087285701470339&ev=PageView&noscript=1\"\n" +
            "/></noscript>\n" +
            "<!-- End Facebook Pixel Code -->\n" +
            "\n" +
            "<!-- Linkedin Pixel Code -->\n" +
            "<script type=\"text/javascript\">\n" +
            "_linkedin_partner_id = \"610825\";\n" +
            "window._linkedin_data_partner_ids = window._linkedin_data_partner_ids || [];\n" +
            "window._linkedin_data_partner_ids.push(_linkedin_partner_id);\n" +
            "</script><script type=\"text/javascript\">\n" +
            "(function(){var s = document.getElementsByTagName(\"script\")[0];\n" +
            "var b = document.createElement(\"script\");\n" +
            "b.type = \"text/javascript\";b.async = true;\n" +
            "b.src = \"https://snap.licdn.com/li.lms-analytics/insight.min.js\";\n" +
            "s.parentNode.insertBefore(b, s);})();\n" +
            "</script>\n" +
            "<noscript>\n" +
            "<img height=\"1\" width=\"1\" style=\"display:none;\" alt=\"\" src=\"https://px.ads.linkedin.com/collect/?pid=610825&fmt=gif\" />\n" +
            "</noscript>\n" +
            "<!-- End Linkedin Pixel Code -->\n" +
            "\n" +
            "    <meta name=\"google-site-verification\" content=\"yxZDsO9N9GjO2Bf5VnB6WlCJyg4-TH6NDIDQgxLv1f4\">\n" +
            "\n" +
            "<script charset=\"utf-8\" src=\"https://platform.twitter.com/js/button.683df8cb64b87a8e4759b1fa17147ad1.js\"></script><script src=\"https://googleads.g.doubleclick.net/pagead/viewthroughconversion/973726542/?random=1598289263341&amp;cv=9&amp;fst=1598289263341&amp;num=1&amp;bg=ffffff&amp;guid=ON&amp;resp=GooglemKTybQhCsO&amp;u_h=900&amp;u_w=1600&amp;u_ah=873&amp;u_aw=1600&amp;u_cd=24&amp;u_his=3&amp;u_tz=120&amp;u_java=false&amp;u_nplug=3&amp;u_nmime=4&amp;gtm=2oa8c0&amp;sendb=1&amp;ig=1&amp;data=event%3Dgtag.config&amp;frm=0&amp;url=https%3A%2F%2Fscrapy.org%2F&amp;ref=https%3A%2F%2Fwww.google.com%2F&amp;tiba=Scrapy%20%7C%20A%20Fast%20and%20Powerful%20Scraping%20and%20Web%20Crawling%20Framework&amp;hn=www.googleadservices.com&amp;async=1&amp;rfmt=3&amp;fmt=4\"></script><script type=\"text/javascript\" src=\"https://scrapinghub.refersion.com/tracker/v3/merchant/pub_187fec375fa525a197a6.js?v=88\"></script></head>\n" +
            "\n" +
            "\n" +
            "  <body>\n" +
            "\n" +
            "    <div class=\"header\">\n" +
            "\n" +
            "<div class=\"container\">\n" +
            "\n" +
            "  <a href=\"https://scrapy.org\" id=\"link-logo\"><div class=\"logo\"></div></a>\n" +
            "\n" +
            "\n" +
            "\n" +
            "  \n" +
            "    <input class=\"menu-toggle\" id=\"menu-toggle\" type=\"checkbox\">\n" +
            "    <label class=\"menu-toggle-button\" for=\"menu-toggle\"><i class=\"fa fa-bars\"></i></label>\n" +
            "    <ul class=\"navigation\">\n" +
            "      \n" +
            "        \n" +
            "        \n" +
            "\n" +
            "        <a class=\"\" href=\"../download/\" id=\"link-download\">\n" +
            "          <li class=\"first  \">\n" +
            "            Download\n" +
            "          </li>\n" +
            "        </a>\n" +
            "      \n" +
            "        \n" +
            "        \n" +
            "\n" +
            "        <a class=\"\" href=\"../doc/\" id=\"link-documentation\">\n" +
            "          <li class=\"  \">\n" +
            "            Documentation\n" +
            "          </li>\n" +
            "        </a>\n" +
            "      \n" +
            "        \n" +
            "        \n" +
            "\n" +
            "        <a class=\"\" href=\"../resources/\" id=\"link-resources\">\n" +
            "          <li class=\"  \">\n" +
            "            Resources\n" +
            "          </li>\n" +
            "        </a>\n" +
            "      \n" +
            "        \n" +
            "        \n" +
            "\n" +
            "        <a class=\"\" href=\"../community/\" id=\"link-community\">\n" +
            "          <li class=\"  \">\n" +
            "            Community\n" +
            "          </li>\n" +
            "        </a>\n" +
            "      \n" +
            "        \n" +
            "        \n" +
            "\n" +
            "        <a class=\"\" href=\"../companies/\" id=\"link-commercial-support\">\n" +
            "          <li class=\"  last\">\n" +
            "            Commercial Support\n" +
            "          </li>\n" +
            "        </a>\n" +
            "      \n" +
            "      <a href=\"http://docs.scrapy.org/en/latest/faq.html\" id=\"link-faq\"><li>FAQ</li></a>\n" +
            "      <a href=\"https://github.com/scrapy/scrapy\" id=\"link-github\">\n" +
            "      <li><i class=\"fa fa-code-fork fa-lg\"></i> <span class=\"github-text\">Fork on Github</span></li>\n" +
            "      </a>\n" +
            "    </ul>\n" +
            "  \n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "\n" +
            "    <div class=\"page-content\">\n" +
            "      <div class=\"wrapper\">\n" +
            "        \n" +
            "\n" +
            "\n" +
            "\n" +
            "<div class=\"container\">\n" +
            "\n" +
            "  <div class=\"first-row\">\n" +
            "\n" +
            "    <div class=\"block-left\">\n" +
            "      <div id=\"scrapy-logo\"></div>\n" +
            "      <p>An open source and collaborative framework for extracting the data you need from websites.\n" +
            "      </p>\n" +
            "      <p>In a fast, simple, yet extensible way.</p>\n" +
            "\n" +
            "      <p class=\"maintained-by\">\n" +
            "        Maintained by\n" +
            "        <a href=\"https://scrapinghub.com/\">\n" +
            "          Scrapinghub\n" +
            "        </a>\n" +
            "        and\n" +
            "        <a href=\"https://github.com/scrapy/scrapy/graphs/contributors\">\n" +
            "          many other contributors\n" +
            "        </a>\n" +
            "      </p>\n" +
            "\n" +
            "    <div class=\"badges-bar\">\n" +
            "  <a href=\"https://pypi.org/project/Scrapy\">\n" +
            "    <img alt=\"PyPI Version\" src=\"https://img.shields.io/pypi/v/Scrapy.svg\" style=\"max-width:100%;\">\n" +
            "  </a>\n" +
            "  <a href=\"https://pypi.org/project/Scrapy\">\n" +
            "    <img alt=\"Wheel Status\" src=\"https://img.shields.io/badge/wheel-yes-brightgreen.svg\" style=\"max-width:100%;\">\n" +
            "  </a>\n" +
            "  <a href=\"https://codecov.io/github/scrapy/scrapy?branch=master\">\n" +
            "    <img alt=\"Coverage report\" src=\"https://img.shields.io/codecov/c/github/scrapy/scrapy/master.svg\" style=\"max-width:100%;\">\n" +
            "  </a>\n" +
            "</div>\n" +
            "\n" +
            "    </div>\n" +
            "\n" +
            "    <div class=\"block-right\">\n" +
            "      <div class=\"big-button\">\n" +
            "  <div class=\"box-title\">Install the latest version of Scrapy</div>\n" +
            "  <div class=\"download-stripe\">\n" +
            "    <p> <i class=\"fa fa-cloud-download \"></i> Scrapy 2.3.0 </p>\n" +
            "  </div>\n" +
            "  <div class=\"install-code\"><span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> <span class=\"command\">pip install scrapy</span></div>\n" +
            "  <div class=\"download-alternatives\">\n" +
            "    <a href=\"https://pypi.org/project/Scrapy/\"><button>PyPI</button></a>\n" +
            "    <a href=\"https://anaconda.org/conda-forge/scrapy\"><button>Conda</button></a>\n" +
            "    <a href=\"https://docs.scrapy.org/en/latest/news.html\"><button>Release Notes</button></a>\n" +
            "  </div>\n" +
            "</div>\n" +
            "\n" +
            "    </div>\n" +
            "\n" +
            "  </div>\n" +
            "</div>\n" +
            "\n" +
            "<div class=\"second-row\">\n" +
            "  <div class=\"container code-box-line\">\n" +
            "    <div class=\"code-box\">\n" +
            "      <div class=\"box-header\">\n" +
            "        <p>Terminal<span class=\"close-btn\">•</span></p>\n" +
            "      </div>\n" +
            "      <div class=\"box-code tab-page active-page\">\n" +
            "        <pre><span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> pip install scrapy\n" +
            "<span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> cat &gt; myspider.py &lt;&lt;EOF\n" +
            "<figure class=\"highlight\"><pre><code class=\"language-python\" data-lang=\"python\"><span class=\"kn\">import</span> <span class=\"nn\">scrapy</span>\n" +
            "\n" +
            "<span class=\"k\">class</span> <span class=\"nc\">BlogSpider</span><span class=\"p\">(</span><span class=\"n\">scrapy</span><span class=\"p\">.</span><span class=\"n\">Spider</span><span class=\"p\">):</span>\n" +
            "    <span class=\"n\">name</span> <span class=\"o\">=</span> <span class=\"s\">'blogspider'</span>\n" +
            "    <span class=\"n\">start_urls</span> <span class=\"o\">=</span> <span class=\"p\">[</span><span class=\"s\">'https://blog.scrapinghub.com'</span><span class=\"p\">]</span>\n" +
            "\n" +
            "    <span class=\"k\">def</span> <span class=\"nf\">parse</span><span class=\"p\">(</span><span class=\"bp\">self</span><span class=\"p\">,</span> <span class=\"n\">response</span><span class=\"p\">):</span>\n" +
            "        <span class=\"k\">for</span> <span class=\"n\">title</span> <span class=\"ow\">in</span> <span class=\"n\">response</span><span class=\"p\">.</span><span class=\"n\">css</span><span class=\"p\">(</span><span class=\"s\">'.post-header&gt;h2'</span><span class=\"p\">):</span>\n" +
            "            <span class=\"k\">yield</span> <span class=\"p\">{</span><span class=\"s\">'title'</span><span class=\"p\">:</span> <span class=\"n\">title</span><span class=\"p\">.</span><span class=\"n\">css</span><span class=\"p\">(</span><span class=\"s\">'a ::text'</span><span class=\"p\">).</span><span class=\"n\">get</span><span class=\"p\">()}</span>\n" +
            "\n" +
            "        <span class=\"k\">for</span> <span class=\"n\">next_page</span> <span class=\"ow\">in</span> <span class=\"n\">response</span><span class=\"p\">.</span><span class=\"n\">css</span><span class=\"p\">(</span><span class=\"s\">'a.next-posts-link'</span><span class=\"p\">):</span>\n" +
            "            <span class=\"k\">yield</span> <span class=\"n\">response</span><span class=\"p\">.</span><span class=\"n\">follow</span><span class=\"p\">(</span><span class=\"n\">next_page</span><span class=\"p\">,</span> <span class=\"bp\">self</span><span class=\"p\">.</span><span class=\"n\">parse</span><span class=\"p\">)</span></code></pre></figure>EOF\n" +
            "<span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> scrapy runspider myspider.py\n" +
            "</pre>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "\n" +
            "    <div class=\"code-subs\"><p>Build and run your<br><span class=\"highlight\">web spiders</span></p></div>\n" +
            "\n" +
            "  </div>\n" +
            "\n" +
            "  <div class=\"container code-box-line\">\n" +
            "    <div class=\"code-box\">\n" +
            "      <div class=\"box-header\">\n" +
            "        <p>Terminal<span class=\"close-btn\">•</span></p>\n" +
            "      </div>\n" +
            "      <div class=\"box-code tab-page active-page\">\n" +
            "        <pre><span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> pip install shub\n" +
            "<span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> shub login\n" +
            "<span class=\"comments\">Insert your Scrapinghub API Key: <span class=\"placeholder\">&lt;API_KEY&gt;</span></span>\n" +
            "\n" +
            "<span class=\"comments\"># Deploy the spider to Scrapy Cloud</span>\n" +
            "<span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> shub deploy\n" +
            "\n" +
            "<span class=\"comments\"># Schedule the spider for execution</span>\n" +
            "<span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> shub schedule blogspider <span class=\"comments\">\n" +
            "Spider blogspider scheduled, watch it running here:\n" +
            "https://app.scrapinghub.com/p/26731/job/1/8</span>\n" +
            "\n" +
            "<span class=\"comments\"># Retrieve the scraped data</span>\n" +
            "<span class=\"prompt\" onselectstart=\"return false\"><i class=\"fa fa-dollar\"></i></span> shub items 26731/1/8\n" +
            "<figure class=\"highlight\"><pre><code class=\"language-python\" data-lang=\"python\"><span class=\"p\">{</span><span class=\"s\">\"title\"</span><span class=\"p\">:</span> <span class=\"s\">\"Improved Frontera: Web Crawling at Scale with Python 3 Support\"</span><span class=\"p\">}</span>\n" +
            "<span class=\"p\">{</span><span class=\"s\">\"title\"</span><span class=\"p\">:</span> <span class=\"s\">\"How to Crawl the Web Politely with Scrapy\"</span><span class=\"p\">}</span>\n" +
            "<span class=\"p\">...</span></code></pre></figure></pre>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "\n" +
            "    <div class=\"code-subs\"><p>Deploy them to<br><a href=\"https://scrapinghub.com/scrapy-cloud/\" title=\"\"><span class=\"highlight\">Scrapy Cloud</span></a></p>\n" +
            "    <p class=\"sub-sub\">or use <a href=\"https://github.com/scrapy/scrapyd\" title=\"Scrapyd\"><span class=\"highlight\">Scrapyd</span></a> to host the spiders on your own server</p></div>\n" +
            "  </div>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "<div class=\"container\">\n" +
            "\n" +
            "  <div class=\"third-row\">\n" +
            "    <div class=\"block-01\">\n" +
            "      <i class=\"fa fa-flash fa-4x\"> </i>\n" +
            "      <h3>Fast and powerful</h3>\n" +
            "      <p>write the rules to extract the data and let Scrapy do the rest</p>\n" +
            "    </div>\n" +
            "    <div class=\"block-02\">\n" +
            "      <i class=\"fa fa-puzzle-piece fa-4x\"> </i>\n" +
            "      <h3>Easily extensible</h3>\n" +
            "      <p>extensible by design, plug new functionality easily without having to touch the core</p>\n" +
            "    </div>\n" +
            "    <div class=\"block-03\">\n" +
            "      <i class=\"fa fa-cubes fa-4x\"> </i>\n" +
            "      <h3>Portable, Python</h3>\n" +
            "      <p>written in Python and runs on Linux, Windows, Mac and BSD</p>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n" +
            "\n" +
            "<div class=\"fourth-row\">\n" +
            "  <div class=\"container\">\n" +
            "    <div class=\"block-left\">\n" +
            "      <h2>Healthy community</h2>\n" +
            "      <ul>\n" +
            "        <li>- 36.3k stars, 8.4k forks and 1.8k watchers on <a href=\"https://github.com/scrapy/scrapy\">GitHub</a></li>\n" +
            "        <li>- 5.1k followers on <a href=\"https://twitter.com/ScrapyProject\">Twitter</a></li>\n" +
            "        <li>- 14.7k questions on <a href=\"http://stackoverflow.com/tags/scrapy/info\">StackOverflow</a></li>\n" +
            "      </ul>\n" +
            "    </div>\n" +
            "    <div class=\"block-right\">\n" +
            "      <h2>Want to know more?</h2>\n" +
            "      <ul>\n" +
            "        <li><a href=\"http://docs.scrapy.org/en/latest/intro/overview.html\">- Discover Scrapy at a glance</a></li>\n" +
            "        <li><a href=\"../companies/\">- Meet the companies using Scrapy</a></li>\n" +
            "      </ul>\n" +
            "\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n" +
            "\n" +
            "      </div>\n" +
            "    </div>\n" +
            "\n" +
            "    <div class=\"footer\">\n" +
            "\t<iframe src=\"https://ghbtns.com/github-btn.html?user=scrapy&amp;repo=scrapy&amp;type=watch&amp;count=true\" allowtransparency=\"true\" frameborder=\"0\" scrolling=\"0\" width=\"110\" height=\"20\"></iframe>\n" +
            "\t<iframe src=\"https://ghbtns.com/github-btn.html?user=scrapy&amp;repo=scrapy&amp;type=fork&amp;count=true\" allowtransparency=\"true\" frameborder=\"0\" scrolling=\"0\" width=\"110\" height=\"20\"></iframe>\n" +
            "\t<iframe id=\"twitter-widget-0\" scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" allowfullscreen=\"true\" class=\"twitter-follow-button twitter-follow-button-rendered\" style=\"position: static; visibility: visible; width: 154px; height: 20px;\" title=\"Twitter Follow Button\" src=\"https://platform.twitter.com/widgets/follow_button.3c5aa8e2a38bbbee4b6d88e6846fc657.en.html#dnt=false&amp;id=twitter-widget-0&amp;lang=en&amp;screen_name=ScrapyProject&amp;show_count=true&amp;show_screen_name=false&amp;size=m&amp;time=1598289263515\" data-screen-name=\"ScrapyProject\"></iframe>\n" +
            "\t<script type=\"text/javascript\">\n" +
            "\twindow.twttr = (function (d, s, id) {\n" +
            "\t  var t, js, fjs = d.getElementsByTagName(s)[0];\n" +
            "\t  if (d.getElementById(id)) return;\n" +
            "\t  js = d.createElement(s); js.id = id;\n" +
            "\t  js.src= \"https://platform.twitter.com/widgets.js\";\n" +
            "\t  fjs.parentNode.insertBefore(js, fjs);\n" +
            "\t  return window.twttr || (t = { _e: [], ready: function (f) { t._e.push(f) } });\n" +
            "\t}(document, \"script\", \"twitter-wjs\"));\n" +
            "\t</script>\n" +
            "\n" +
            " \t<p>Maintained by <a href=\"https://scrapinghub.com/\">Scrapinghub</a> and <a href=\"https://github.com/scrapy/scrapy/graphs/contributors\">many other contributors</a></p>\n" +
            "</div>\n" +
            "\n" +
            "\n" +
            "  \n" +
            "\n" +
            "\n" +
            "<iframe scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" src=\"https://platform.twitter.com/widgets/widget_iframe.3c5aa8e2a38bbbee4b6d88e6846fc657.html?origin=https%3A%2F%2Fscrapy.org\" title=\"Twitter settings iframe\" style=\"display: none;\"></iframe><div><iframe id=\"cross-domain-iframe\" src=\"https://scrapinghub.refersion.com/tracker/v3/xdomain/pub_187fec375fa525a197a6.html\" style=\"display: none;\"></iframe></div></body></html>"
            ;

}
