Author: Zaid Al-Momen
Date: 01/25/2018
===================

News Search App documentation. 
------------------------------ 

This is a news search application designed using javaFX and tomcat servlet technology. There are two projects to set up. 

1) the NewsApp JavaFX UI: this is the main application user interface. Users will be able to type in a search item and search for results from the news-api.org website. Results can be saved to the local tomcat server/database and deleted 

2) the DemoApp web application deployed on tomcat, with a derby database. This application runs in the background to connect to the local database and store/delete news results. 


SETUP:
1. Launch NetBeans and Open project... -> Select the NewsApp JavaFX UI project. 
2. Open Project -> Select DemoApp apache tomcat servlet project. 
3. Make sure you have an apache tomcat instance installed under "Services" tab in netbeans. 
4. Under services tab, databases/javaDB, Create a DERBY database called "news". leave username and password blanks. Connect to DB.  
5. under the DemoApp project, find tables.sql and open file. Select the 'news' connection and run all SQL statements to create NEWS_RESULT table.  


RUNNING PROJECTS: 

1. Right click DemoApp project and click run. Application should build and run on the tomcat server. It will try to open a chrome window with the main page... 

2. in netbeans, right click NewsApp and click run. The main JavaFX app should run. 


HOW TO USE:
1. in the main JavaFX app, type a search item in the box. Examples: bitcoin, united states, netflix, .. or any other search item. 
2. hit ENTER or click the Go Button. 
3. You should see results start to populate on the main page. 
4. hit the checkboxes next to each result and click "Save Selected"
5. Results will be saved to the "View Saved" menu item. 
6. Click "View Saved" to view saved results. 
7. use checkboxes to select saved results. Click "Delete Selected" and the selected results will be deleted. 
8. Click the main menu button again and type another search item, results will be updated. Continue to save/delete items.

** NOTE: the popup action per each result item does not work properly. currently it causes the existing results to disappear temporarily, click the main menu button or view saved button to retrieve results again. 
pop up window will show more details (title, summary and author...)

9. See sample Screenshots. 



