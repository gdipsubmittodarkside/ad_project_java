README.MD

## Team 2 AD (19 Feb 2023)
How to run the programme locally

##### Set up and populate MySQL DB
1. Open spring boot app. Check that application.properties =  **create-drop** for db
2. Run app once. This will remove all data in your DB and fill in 86 skills in Skill Table and 1 member
3. After this, change from create-drop to **update**, and **comment out command line runner**.

4. Open python app. Check that you have these dependencies:
* beautiful soup 4
* relativedelta
* embeddify
* selenium
* jsonify
* pip install --upgrade google-api-python-client
* pip install --upgrade google-auth-oauthlib google-auth-httplib2
* pip install chromedriver-py
* pip install --upgrade google-api-python-client google-auth-httplib2 google-auth-oauthlib

5. Delete token.pickle (if it your first time opening the app)

6. Run web_scraper.py
* Log in with this gmail account:
email: sa55team2ad@gmail.com
password: team2ad 
* if your python server is running successfully, there will be an automatic browser pop-up ("data;"). typing "localhost:8089/search?query=html" and "localhost:8089//request_coursedata/?keyword=java" should also return something.

7. Run spring boot app
* ensure application.properties = update for db and command line runner in Main is commented out.

8. In browser type "localhost:8080/call_python/getAllJobs/me" 
* This should populate your "Job" (17 rows) and "JobSkills" table in DB

9. In browser type "localhost:8080/call_python/getAllCourses/me"
* This should populate your "CourseCrawled" table in DB (4000+ rows)

10. DB is now populated. Log in with "techs" (both username & password) or with google to start using the app. Mobile should be working now too. 

##### Deployed web-app
http://techsmvp-env.eba-e3cesppi.ap-southeast-1.elasticbeanstalk.com/