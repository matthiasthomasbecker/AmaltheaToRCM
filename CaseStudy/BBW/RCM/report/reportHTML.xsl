<?xml version="1.0" encoding="utf-8"?>
<!--
  Copyright Arcticus Systems AB, All rights reserved.
  The format and content in the File is protected by copyright.
  The File is furnished under a licence and may only be used in
  accordance with the terms of this licence.
-->
<xsl:stylesheet 
    version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
	<xsl:output 
        version="1.0" 
        method="html" 
        encoding="utf-8"
		indent="yes" 
        />

<!-- Root match -->
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
				<link href="./report.css" type="text/css" rel="stylesheet" />
			</head>
			<body>
				<div class="header">
					<img src="./images/logo-arcticus.png" alt="Logo" />
					<b>Mastering Software Complexity</b>
				</div>

				<div class="main">
				<xsl:apply-templates select="document" />
				</div>

				<div class="footer">
					<p>Copyright 1995-2019 Arcticus Systems AB. All rights reserved.</p>
				</div>
			</body>
		</html>
	</xsl:template>

<!-- document match -->
	<xsl:template match="document">
		<xsl:apply-templates select="Header" />
		<xsl:apply-templates select="Section">
            <xsl:sort data-type="number" select="@Index" />
		</xsl:apply-templates>
		<xsl:apply-templates select="Footer" />
	</xsl:template>

<!-- Header match -->
	<xsl:template match="Header">
		<title><xsl:value-of select="@Plugin" /></title>

		<h1><xsl:value-of select="@Name" /></h1>

		<p>
        <xsl:if test="@Date">
			Date Created: <xsl:value-of select="@Date" /><br/>
        </xsl:if>

        <xsl:if test="@Time">
            Time Created: <xsl:value-of select="@Time" /><br/>
        </xsl:if>

        <xsl:if test="@TimeExecution">
            Execution Time: <xsl:value-of select="@TimeExecution" /><br/>
        </xsl:if>

        <xsl:if test="@Author">
			  Author: <xsl:value-of select="@Author" /><br/>
        </xsl:if>

        <xsl:if test="@User">
			  User: <xsl:value-of select="@User" /><br/>
        </xsl:if>

        <xsl:if test="@Description">
  			Description: <xsl:value-of select="@Description" /><br/>
        </xsl:if>

        <xsl:if test="@Version">
            Version: <xsl:value-of select="@Version" /><br/>
        </xsl:if>
        </p>
	</xsl:template>

<!-- SectionItem match -->
    <xsl:template match="SectionItem">
        <xsl:apply-templates select="Section"/>
        <xsl:apply-templates select="Table" />
        <xsl:apply-templates select="Image" />
        <xsl:apply-templates select="List" />
        <xsl:apply-templates select="Paragraph" />
    </xsl:template>

<!-- Section match -->
    <xsl:template match="Section">
        <xsl:variable name="HeadingStyle" select="concat('h', @HeadingLevel)" />
        <xsl:element name="{$HeadingStyle}"><xsl:value-of select="@HeadingText" /></xsl:element>
		<xsl:apply-templates select="Section | SectionItem">
            <xsl:sort data-type="number" select="@Index" />
		</xsl:apply-templates>
    </xsl:template>

<!-- Table match -->
    <xsl:template match="Table">
        <h3><xsl:value-of select="@Name" /></h3>

		<table>
			<xsl:if test="@WidthPercent">
				<xsl:attribute name="width"><xsl:value-of select="@WidthPercent" /></xsl:attribute>
			</xsl:if>
            
			<tr>
				<xsl:for-each select="Columns/Column">
					<xsl:sort data-type="number" select="@Index" />
					<th>
						<xsl:if test="@WidthPercent">
							<xsl:attribute name="width"><xsl:value-of select="@WidthPercent" /></xsl:attribute>
						</xsl:if>
						<xsl:value-of select="@Name" />
					</th>
				</xsl:for-each>
			</tr>

			<xsl:for-each select="Row">
				<xsl:sort data-type="number" select="@Index" />
				<tr>
					<xsl:for-each select="Column">
		                <xsl:sort data-type="number" select="@Index" />
						<td>
							<xsl:if test="@Color">
								<xsl:attribute name="bgcolor"><xsl:value-of select="@Color" /></xsl:attribute>
								<xsl:attribute name="class">color</xsl:attribute>
							</xsl:if>
                            
							<xsl:choose>
								<xsl:when test="Text"><xsl:apply-templates select="Text" /></xsl:when>
								<xsl:when test="Link"><xsl:apply-templates select="Link" /></xsl:when>
							</xsl:choose>
						</td>
					</xsl:for-each>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

<!-- Image match -->
	<xsl:template match="Image">
		<img src="{@Path}" alt="{@Name}"/>
	</xsl:template>

<!-- List match -->
	<xsl:template match="List">
		<ul>
			<xsl:for-each select="Row">
				<xsl:sort data-type="number" select="@Index" />
				<li>
					<xsl:choose>
						<xsl:when test="Text"><xsl:apply-templates select="Text" /></xsl:when>
                        <xsl:when test="Link"><xsl:apply-templates select="Link" /></xsl:when>
					</xsl:choose>
				</li>
			</xsl:for-each>
		</ul>
	</xsl:template>

<!-- Paragraph match -->
	<xsl:template match="Paragraph">
		<p>
		<xsl:choose>
			<xsl:when test="Text"><xsl:apply-templates select="Text" /></xsl:when>
            <xsl:when test="Link"><xsl:apply-templates select="Link" /></xsl:when>
		</xsl:choose>
		</p>
	</xsl:template>

<!-- Link match -->
	<xsl:template match="Link">
		<a href="{@Path}"><xsl:value-of select="@Name" /></a>
	</xsl:template>

<!-- Text match -->
	<xsl:template match="Text">
		<xsl:value-of select="@Text" />
	</xsl:template>

</xsl:stylesheet>
