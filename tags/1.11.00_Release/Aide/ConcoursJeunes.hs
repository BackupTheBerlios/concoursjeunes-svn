<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE helpset PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN" "../dtd/helpset_2_0.dtd">
<helpset version="1.0">
	<title>Aide Gestion des Concours Jeunes</title>
	<maps>
		<homeID>top</homeID>
		<mapref location="ConcoursJeunes.jhm"/>
	</maps>
	<view>
		<name>TOC</name>
		<label>Table des Matieres</label>
		<type>javax.help.TOCView</type>
		<data>TOC.xml</data>
	</view>
<!--	<view>
		<name>Index</name>
		<label>Index</label>
		<type>javax.help.IndexView</type>
		<data>Index.xml</data>
	</view>
	<view>
		<name>Search</name>
		<label>Recherche</label>
		<type>javax.help.SearchView</type>
		<data engine="com.sun.java.help.search.DefaultSearchEngine">
			JavaHelpSearch
		</data>
	</view>-->
	<presentation default="true" displayviewimages="false">
		<name>main window</name>
		<size width="700" height="400" />
		<location x="200" y="200" />
		<title>Aide Gestion des Concours Jeunes</title>
		<image>toplevelfolder</image>
		<toolbar>
			<helpaction> javax.help.BackAction</helpaction>
			<helpaction> javax.help.ForwardAction</helpaction>
			<helpaction> javax.help.SeparatorAction</helpaction>
			<helpaction> javax.help.HomeAction</helpaction>
			<helpaction> javax.help.ReloadAction</helpaction>
			<helpaction> javax.help.SeparatorAction</helpaction>
			<helpaction> javax.help.PrintAction</helpaction>
			<helpaction> javax.help.PrintSetupAction</helpaction>
		</toolbar>
	</presentation>
	<presentation>
		<name>main</name>
		<size width="400" height="400" />
		<location x="200" y="200" />
		<title>Aide Gestion des Concours Jeunes</title>
	</presentation>
</helpset>