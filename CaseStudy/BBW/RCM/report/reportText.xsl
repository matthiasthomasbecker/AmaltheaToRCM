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
        method="text"
        encoding="utf-8"
		indent="yes"
        />
   
    <!-- Root match -->
    <xsl:template match="/">
        <xsl:apply-templates select="document" />
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
        <xsl:value-of select="@Plugin" />
        <xsl:value-of select="@Name" />

        <xsl:if test="@Date">
            Date Created: <xsl:value-of select="@Date" />
        </xsl:if>

        <xsl:if test="@Time">
            Time Created: <xsl:value-of select="@Time" />
        </xsl:if>

        <xsl:if test="@TimeExecution">
            Execution Time: <xsl:value-of select="@TimeExecution" />
        </xsl:if>

        <xsl:if test="@Author">
            Author: <xsl:value-of select="@Author" />
        </xsl:if>

        <xsl:if test="@User">
            User: <xsl:value-of select="@User" />
        </xsl:if>

        <xsl:if test="@Description">
            Description: <xsl:value-of select="@Description" />
        </xsl:if>

        <xsl:if test="@Version">
            Version: <xsl:value-of select="@Version" />
        </xsl:if>
        <xsl:text>&#xD;</xsl:text>
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
        <xsl:element name="{$HeadingStyle}">
            <xsl:text>&#xD;</xsl:text>
            <xsl:value-of select="@HeadingText" />
            <xsl:text>&#xD;</xsl:text>
        </xsl:element>
        <xsl:apply-templates select="Section | SectionItem">
            <xsl:sort data-type="number" select="@Index" />
        </xsl:apply-templates>
    </xsl:template>

    <!-- Table match -->
    <xsl:template match="Table">
        <xsl:text>TABLE: </xsl:text> 
        <xsl:value-of select="@Name" /> 
        <xsl:text>&#xD;</xsl:text> 

        <xsl:apply-templates select="Columns/Column">
            <xsl:sort data-type="number" select="@Index" />
        </xsl:apply-templates>   
        <xsl:text>&#xD;</xsl:text>

        <xsl:for-each select="Row">
            <xsl:sort data-type="number" select="@Index" />
            <xsl:for-each select="Column">
                <xsl:sort data-type="number" select="@Index" />
                <xsl:choose>
                    <xsl:when test="Text">
                        <xsl:apply-templates select="Text" />
                    </xsl:when>
                    <xsl:when test="Link">
                         <xsl:apply-templates select="Link" />
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each>
            <xsl:text>&#xD;</xsl:text>
        </xsl:for-each>       
    </xsl:template>

    <!-- Image match -->
    <xsl:template match="Image">
        <img src="{@Path}" alt="{@Name}"/>
    </xsl:template>

    <!-- List match -->
    <xsl:template match="List">
        <xsl:for-each select="Row">
            <xsl:sort data-type="number" select="@Index" />
            <xsl:choose>
                <xsl:when test="Text">
                    <xsl:apply-templates select="Text" />
                </xsl:when>
                <xsl:when test="Link">
                    <xsl:apply-templates select="Link" />
                </xsl:when>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>

    <!-- Paragraph match -->
    <xsl:template match="Paragraph">
        <xsl:choose>
            <xsl:when test="Text">
                <xsl:apply-templates select="Text" />
            </xsl:when>
            <xsl:when test="Link">
                <xsl:apply-templates select="Link" />
            </xsl:when>
        </xsl:choose>
        <xsl:text>&#xD;</xsl:text>
    </xsl:template>

    <!-- Link match -->
    <xsl:template match="Link">
        <xsl:value-of select="@Name" />
    </xsl:template>

    <!-- Text match -->
    <xsl:template match="Text">
        <xsl:value-of select="@Text" />
    </xsl:template>
 
    <!-- Table cells-->
    <xsl:variable name="maxTableWidth" select="150"/>
    
    <xsl:template match="Row/Column/Link">
        <xsl:variable name="columnIndex" select="../@Index"/>
        <xsl:variable name="width" select='translate(../../../Columns/Column[@Index=$columnIndex]/@WidthPercent,"%","")'/>
        <xsl:call-template name="write-table-cell">
            <xsl:with-param name="string" select="@Name"/>
            <xsl:with-param name="maxWidth" select="$maxTableWidth * $width div 100"/>    
        </xsl:call-template>     
    </xsl:template>
    
    <xsl:template match="Row/Column/Text">
        <xsl:variable name="columnIndex" select="../@Index"/>
        <xsl:variable name="width" select='translate(../../../Columns/Column[@Index=$columnIndex]/@WidthPercent,"%","")'/>
        <xsl:call-template name="write-table-cell">
            <xsl:with-param name="string" select="@Text"/>
            <xsl:with-param name="maxWidth" select="$maxTableWidth * $width div 100"/>    
        </xsl:call-template>     
    </xsl:template>

    <xsl:template match="Columns/Column">
        <xsl:variable name="width" select='translate(@WidthPercent,"%","")'/>
        <xsl:call-template name="write-table-cell">
            <xsl:with-param name="string" select="@Name"/>
            <xsl:with-param name="maxWidth" select="$maxTableWidth * $width div 100"/>    
        </xsl:call-template>     
    </xsl:template>
    
    <xsl:template name="write-table-cell">
        <xsl:param name="string" select="0"/>
        <xsl:param name="maxWidth" select="30"/>
        <xsl:variable name="padding">
             <xsl:value-of select="$maxWidth - string-length($string)"/>
        </xsl:variable>
        <xsl:value-of select="$string"/>
        <xsl:call-template name="append-pad">
            <xsl:with-param name="length" select="$padding"/>
        </xsl:call-template>      
    </xsl:template>
    
    <xsl:template name="append-pad">
        <xsl:param name="length" select="0"/>
        <xsl:if test="$length > 0">
            <xsl:value-of select="'&#32;'"/>
            <xsl:call-template name="append-pad">
                <xsl:with-param name="length" select="$length - 1"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>
