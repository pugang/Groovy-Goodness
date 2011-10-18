&lt;?xml version="1.0" encoding="UTF-8"?&gt;
&lt;xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"&gt;
    &lt;xsl:output method="html" indent="yes"/&gt;
    &lt;xsl:decimal-format decimal-separator="." grouping-separator=","/&gt;

    &lt;xsl:key name="packageSummary" match="PackageSummary/MetricResult" use="@name"/&gt;

    &lt;xsl:template match="GMetrics"&gt;
        &lt;html&gt;
            &lt;head&gt;
                &lt;title&gt;&lt;xsl:value-of select="Project/@title"/&gt;&lt;/title&gt;
                &lt;link rel="stylesheet" href="default.css" type="text/css"/&gt;
            &lt;/head&gt;
            &lt;body&gt;
                &lt;a name="top"&gt;&lt;/a&gt;

                &lt;div class="header"&gt;
                    &lt;h1&gt;
                        &lt;img src="haki-logo.png" width="48" height="48"/&gt;
                        &lt;xsl:value-of select="Project/@title"/&gt;
                     &lt;/h1&gt;
                &lt;/div&gt;

                &lt;div class="rule"&gt;&lt;/div&gt;

                &lt;div class="summary"&gt;
                    &lt;div class="first"&gt;
                        &lt;dl&gt;
                            &lt;dt&gt;Generated&lt;/dt&gt;
                            &lt;dd&gt;&lt;xsl:value-of select="Report/@timestamp"/&gt;&lt;/dd&gt;
                        &lt;/dl&gt;
                        &lt;dl&gt;
                            &lt;dt&gt;Tool&lt;/dt&gt;
                            &lt;dd&gt;&lt;a href="{@url}"&gt;&lt;xsl:value-of select="@version"/&gt;&lt;/a&gt;&lt;/dd&gt;
                        &lt;/dl&gt;

                        &lt;dl&gt;
                            &lt;dt&gt;Sources&lt;/dt&gt;
                            &lt;dl&gt;
                            &lt;xsl:for-each select="Project/SourceDirectory"&gt;
                                &lt;xsl:value-of select="."/&gt;
                                &lt;br /&gt;
                            &lt;/xsl:for-each&gt;
                            &lt;/dl&gt;
                        &lt;/dl&gt;
                    &lt;/div&gt;
                    &lt;div class="last"&gt;
                        &lt;xsl:apply-templates select="PackageSummary"/&gt;
                    &lt;/div&gt;
                &lt;/div&gt;

                &lt;div class="rule"&gt;&lt;/div&gt;

                &lt;div id="top-lists"&gt;
                    &lt;div class="first"&gt;
                        &lt;xsl:apply-templates select="." mode="top-classes"/&gt;
                    &lt;/div&gt;
                    &lt;div class="last"&gt;
                        &lt;xsl:apply-templates select="." mode="top-methods"/&gt;
                    &lt;/div&gt;
                &lt;/div&gt;

                &lt;div class="rule"&gt;&lt;/div&gt;

                &lt;xsl:apply-templates select="." mode="full"/&gt;

                &lt;div class="rule"&gt;&lt;/div&gt;

                &lt;xsl:apply-templates select="Metrics"/&gt;

                &lt;div class="rule"&gt;&lt;/div&gt;

                &lt;p&gt;XSLT created by &lt;a href="http://www.mrhaki.com"&gt;Hubert A. Klein Ikkink (mrhaki)&lt;/a&gt;&lt;/p&gt;

            &lt;/body&gt;
        &lt;/html&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template match="PackageSummary"&gt;
        &lt;ul id="summary_stats"&gt;
            &lt;li&gt;
                &lt;strong&gt;
                    &lt;xsl:call-template name="display_value"&gt;
                        &lt;xsl:with-param name="value" select="MetricResult[@name = 'ABC']/@average"/&gt;
                    &lt;/xsl:call-template&gt;
                &lt;/strong&gt;
                &lt;span&gt;ABC&lt;/span&gt;
            &lt;/li&gt;
            &lt;li&gt;
                &lt;strong&gt;
                    &lt;xsl:call-template name="display_value"&gt;
                        &lt;xsl:with-param name="value" select="MetricResult[@name = 'CyclomaticComplexity']/@average"/&gt;
                    &lt;/xsl:call-template&gt;
                &lt;/strong&gt;
                &lt;span&gt;cyclomatic complexity&lt;/span&gt;
            &lt;/li&gt;
            &lt;li&gt;
                &lt;strong&gt;
                    &lt;xsl:call-template name="display_value"&gt;
                        &lt;xsl:with-param name="value" select="MetricResult[@name = 'MethodLineCount']/@average"/&gt;
                    &lt;/xsl:call-template&gt;
                &lt;/strong&gt;
                &lt;span&gt;method lines&lt;/span&gt;
            &lt;/li&gt;
            &lt;li&gt;
                &lt;strong&gt;
                    &lt;xsl:call-template name="display_value"&gt;
                        &lt;xsl:with-param name="value" select="MetricResult[@name = 'ClassLineCount']/@average"/&gt;
                    &lt;/xsl:call-template&gt;
                &lt;/strong&gt;
                &lt;span&gt;class lines&lt;/span&gt;
            &lt;/li&gt;
        &lt;/ul&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template match="Metrics"&gt;
        &lt;h2&gt;Metrics&lt;/h2&gt;

        &lt;table border="0" width="100%" cellpadding="0" cellspacing="0"&gt;
        &lt;tr&gt;
            &lt;th&gt;Name&lt;/th&gt;
            &lt;th&gt;Description&lt;/th&gt;
            &lt;th&gt;Average&lt;/th&gt;
            &lt;th&gt;Total&lt;/th&gt;
            &lt;th&gt;Maximum&lt;/th&gt;
            &lt;th&gt;Minimum&lt;/th&gt;
        &lt;/tr&gt;

        &lt;xsl:apply-templates select="Metric"/&gt;

        &lt;/table&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template match="Metric"&gt;
        &lt;xsl:variable name="metricName" select="@name"/&gt;
        &lt;xsl:variable name="summary" select="key('packageSummary', $metricName)"/&gt;
        &lt;tr&gt;
            &lt;td&gt;
                &lt;a name="m-{$metricName}"&gt; &lt;/a&gt;
                &lt;xsl:value-of select="$metricName"/&gt;
            &lt;/td&gt;
            &lt;td&gt;
                &lt;xsl:value-of select="Description"/&gt;
            &lt;/td&gt;
            &lt;td class="number"&gt;
                &lt;xsl:call-template name="display_value"&gt;
                    &lt;xsl:with-param name="value" select="$summary/@average"/&gt;
                &lt;/xsl:call-template&gt;
            &lt;/td&gt;
            &lt;td class="number"&gt;
                &lt;xsl:call-template name="display_value"&gt;
                    &lt;xsl:with-param name="value" select="$summary/@total"/&gt;
                &lt;/xsl:call-template&gt;
            &lt;/td&gt;
            &lt;td class="number"&gt;
                &lt;xsl:call-template name="display_value"&gt;
                    &lt;xsl:with-param name="value" select="$summary/@maximum"/&gt;
                &lt;/xsl:call-template&gt;
            &lt;/td&gt;
            &lt;td class="number"&gt;
                &lt;xsl:call-template name="display_value"&gt;
                    &lt;xsl:with-param name="value" select="$summary/@minimum"/&gt;
                &lt;/xsl:call-template&gt;
            &lt;/td&gt;
        &lt;/tr&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template match="GMetrics" mode="top-methods"&gt;
        &lt;h2&gt;Top Complexity Methods&lt;/h2&gt;

        &lt;table border="0" cellpadding="0" cellspacing="0"&gt;
        &lt;tr&gt;
            &lt;th&gt;Name&lt;/th&gt;
            &lt;th&gt;Complexity&lt;/th&gt;
        &lt;/tr&gt;
        &lt;xsl:for-each select="//Method/MetricResult[@name = 'CyclomaticComplexity']"&gt;
            &lt;xsl:sort select="@total" data-type="number" order="descending"/&gt;
            &lt;xsl:if test="position() &amp;lt;= 7"&gt;
                &lt;tr&gt;
                    &lt;td&gt;
                        &lt;a href="#method-{../../@name}-{../@name}"&gt;
                            &lt;xsl:value-of select="../@name"/&gt;
                        &lt;/a&gt;
                    &lt;/td&gt;
                    &lt;td&gt;&lt;xsl:value-of select="@total"/&gt;&lt;/td&gt;
                &lt;/tr&gt;
            &lt;/xsl:if&gt;
        &lt;/xsl:for-each&gt;
        &lt;/table&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template match="GMetrics" mode="top-classes"&gt;
        &lt;h2&gt;Top Complexity Classes&lt;/h2&gt;

        &lt;table border="0" cellpadding="0" cellspacing="0"&gt;
        &lt;tr&gt;
            &lt;th&gt;Name&lt;/th&gt;
            &lt;th&gt;Complexity&lt;/th&gt;
        &lt;/tr&gt;
        &lt;xsl:for-each select="//Class/MetricResult[@name = 'CyclomaticComplexity']"&gt;
            &lt;xsl:sort select="@total" data-type="number" order="descending"/&gt;
            &lt;xsl:if test="position() &amp;lt;= 7"&gt;
                &lt;tr&gt;
                    &lt;td&gt;
                        &lt;a href="#c-{../@name}"&gt;
                            &lt;xsl:value-of select="../@name"/&gt;
                        &lt;/a&gt;
                    &lt;/td&gt;
                    &lt;td&gt;&lt;xsl:value-of select="@total"/&gt;&lt;/td&gt;
                &lt;/tr&gt;
            &lt;/xsl:if&gt;
        &lt;/xsl:for-each&gt;
        &lt;/table&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template match="GMetrics" mode="full"&gt;
        &lt;xsl:for-each select="Package/Class"&gt;
            &lt;xsl:sort select="@name"/&gt;
            &lt;xsl:apply-templates select="." mode="full"/&gt;
            &lt;p/&gt;
            &lt;p/&gt;
        &lt;/xsl:for-each&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template match="Class" mode="summary"&gt;
        &lt;xsl:variable name="violationCount" select="count(Violation)"/&gt;
        &lt;tr&gt;
            &lt;td&gt;&lt;a href="#c-{@name}"&gt;&lt;xsl:value-of select="../@path"/&gt;/&lt;xsl:value-of select="@name"/&gt;&lt;/a&gt;&lt;/td&gt;
            &lt;td&gt;&lt;xsl:value-of select="$violationCount"/&gt;&lt;/td&gt;
        &lt;/tr&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template match="Class" mode="full"&gt;
        &lt;xsl:variable name="classLineCount" select="MetricResult[@name = 'ClassLineCount']"/&gt;
        &lt;xsl:variable name="methodLineCount" select="MetricResult[@name = 'MethodLineCount']"/&gt;
        &lt;xsl:variable name="abc" select="MetricResult[@name = 'ABC']"/&gt;
        &lt;xsl:variable name="cyclomaticComplexity" select="MetricResult[@name = 'CyclomaticComplexity']"/&gt;
        &lt;a name="c-{@name}"&gt;&lt;/a&gt;
        &lt;h2&gt;
            Class
            &lt;xsl:value-of select="@name"/&gt;
        &lt;/h2&gt;

        &lt;div class="class_summary"&gt;
            &lt;table border="0" cellpadding="0" cellspacing="0" width="100%"&gt;
                &lt;tbody&gt;
                    &lt;tr&gt;
                        &lt;td&gt;
                            &lt;a href="#m-ClassLineCount"&gt;
                                &lt;h3&gt;Class line count&lt;/h3&gt;
                            &lt;/a&gt;
                            &lt;p&gt;
                                &lt;strong&gt;
                                    &lt;xsl:call-template name="display_value"&gt;
                                        &lt;xsl:with-param name="value" select="$classLineCount/@total"/&gt;
                                        &lt;xsl:with-param name="format" select="'####0'"/&gt;
                                    &lt;/xsl:call-template&gt;
                                &lt;/strong&gt;
                            &lt;/p&gt;
                        &lt;/td&gt;
                        &lt;td&gt;
                            &lt;a href="#m-MethodLineCount"&gt;
                                &lt;h3&gt;Methods&lt;/h3&gt;
                            &lt;/a&gt;
                            &lt;p&gt;
                                &lt;strong&gt;
                                    &lt;xsl:value-of select="count(Method)"/&gt;
                                &lt;/strong&gt;
                            &lt;/p&gt;
                            &lt;dl&gt;
                                &lt;dt&gt;Average LC:&lt;/dt&gt;
                                &lt;dd&gt;
                                    &lt;xsl:call-template name="display_value"&gt;
                                        &lt;xsl:with-param name="value" select="$methodLineCount/@average"/&gt;
                                    &lt;/xsl:call-template&gt;
                                &lt;/dd&gt;
                                &lt;dt&gt;Maximum LC:&lt;/dt&gt;
                                &lt;dd&gt;
                                    &lt;xsl:call-template name="display_value"&gt;
                                        &lt;xsl:with-param name="value" select="$methodLineCount/@total"/&gt;
                                    &lt;/xsl:call-template&gt;
                                &lt;/dd&gt;
                            &lt;/dl&gt;
                        &lt;/td&gt;
                        &lt;td&gt;
                            &lt;a href="#m-CyclomaticComplexity"&gt;
                                &lt;h3&gt;Complexity&lt;/h3&gt;
                            &lt;/a&gt;
                            &lt;p&gt;
                                &lt;strong&gt;
                                    &lt;xsl:call-template name="display_value"&gt;
                                        &lt;xsl:with-param name="value" select="$cyclomaticComplexity/@average"/&gt;
                                    &lt;/xsl:call-template&gt;
                                &lt;/strong&gt;
                            &lt;/p&gt;
                            &lt;dl&gt;
                                &lt;dt&gt;Maximum:&lt;/dt&gt;
                                &lt;dd&gt;
                                    &lt;xsl:call-template name="display_value"&gt;
                                        &lt;xsl:with-param name="value" select="$cyclomaticComplexity/@maximum"/&gt;
                                    &lt;/xsl:call-template&gt;
                                &lt;/dd&gt;
                            &lt;/dl&gt;
                        &lt;/td&gt;
                        &lt;td&gt;
                            &lt;a href="#m-ABC"&gt;
                                &lt;h3&gt;ABC&lt;/h3&gt;
                            &lt;/a&gt;
                            &lt;p&gt;
                                &lt;strong&gt;
                                    &lt;xsl:call-template name="display_value"&gt;
                                        &lt;xsl:with-param name="value" select="$abc/@average"/&gt;
                                    &lt;/xsl:call-template&gt;
                                &lt;/strong&gt;
                            &lt;/p&gt;
                            &lt;dl&gt;
                                &lt;dt&gt;Maximum:&lt;/dt&gt;
                                &lt;dd&gt;
                                    &lt;xsl:call-template name="display_value"&gt;
                                        &lt;xsl:with-param name="value" select="$abc/@maximum"/&gt;
                                    &lt;/xsl:call-template&gt;
                                &lt;/dd&gt;
                            &lt;/dl&gt;
                        &lt;/td&gt;
                    &lt;/tr&gt;
                &lt;/tbody&gt;
            &lt;/table&gt;
        &lt;/div&gt;

        &lt;xsl:if test="count(Method) &amp;gt; 0"&gt;
        &lt;table border="0" width="100%" cellpadding="0" cellspacing="0"&gt;
            &lt;tr&gt;
                &lt;th&gt;Method&lt;/th&gt;
                &lt;th&gt;Cyclomatic Complexity&lt;/th&gt;
                &lt;th&gt;ABC&lt;/th&gt;
                &lt;th&gt;Lines&lt;/th&gt;
            &lt;/tr&gt;
            &lt;xsl:for-each select="Method"&gt;
                &lt;xsl:sort select="MetricResult[@name = 'CyclomaticComplexity']/@total"/&gt;
                &lt;tr&gt;
                    &lt;td&gt;
                        &lt;a name="method-{../@name}-{@name}"&gt;&lt;/a&gt;
                        &lt;xsl:value-of select="@name"/&gt;
                    &lt;/td&gt;
                    &lt;td&gt;
                        &lt;xsl:value-of select="MetricResult[@name = 'CyclomaticComplexity']/@total"/&gt;
                    &lt;/td&gt;
                    &lt;td&gt;
                        &lt;xsl:value-of select="MetricResult[@name = 'ABC']/@total"/&gt;
                    &lt;/td&gt;
                    &lt;td&gt;
                        &lt;xsl:value-of select="MetricResult[@name = 'MethodLineCount']/@total"/&gt;
                    &lt;/td&gt;
                &lt;/tr&gt;
            &lt;/xsl:for-each&gt;
        &lt;/table&gt;
        &lt;/xsl:if&gt;
        &lt;a href="#top"&gt;Back to top&lt;/a&gt;
    &lt;/xsl:template&gt;

    &lt;xsl:template name="display_value"&gt;
        &lt;xsl:param name="value"/&gt;
        &lt;xsl:param name="format" select="'####.0'"/&gt;
        &lt;xsl:choose&gt;
            &lt;xsl:when test="string($value) != ''"&gt;
                &lt;xsl:value-of select="format-number($value, $format)"/&gt;
            &lt;/xsl:when&gt;
            &lt;xsl:otherwise&gt;
                &lt;xsl:value-of select="'N/A'"/&gt;
            &lt;/xsl:otherwise&gt;
        &lt;/xsl:choose&gt;
    &lt;/xsl:template&gt;
&lt;/xsl:stylesheet&gt;
