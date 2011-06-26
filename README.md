#TiddlyBox

If you use TiddlyWiki by storing it on your Dropbox, this webapp will let you read
and update the wiki from a computer that does not have the Dropbox client installed.

## Getting started

### See a live demo
I have a demo setup on EC2 at [http://tiddlybox.horsefire.com/](http://tiddlybox.horsefire.com/)

### Quickstart if you have Maven
1. Download the source
2. Run: `mvn -Dappkey=9vc7omt80m63p6q -Dappsecret=27riv02ia4c92hf jetty:run`
3. Open [http://localhost:8080](http://localhost:8080)

### Real deployment
The webapp is a simple WAR file with no database requirements. It must run with an OAuth
keypair though. Go to [Dropbox developer zone](https://www.dropbox.com/developers/apps) to
register an app and generate your own keypair, or use my test pair (see below).

To run the app, download the WAR file, deploy it in Jetty or Tomcat, and set the following
environment variables:

 - `appkey`
 - `appsecret`

I have a test pair generated for this project, so if you don't want to generate your own, use:

 - `appkey=9vc7omt80m63p6q`
 - `appsecret=27riv02ia4c92hf`