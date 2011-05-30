<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><title>TiddlyBox</title></head>
  <body>
    <p>This webapp assumes you have a
    <a href="http://www.tiddlywiki.com">TiddlyWiki</a> saved to your
    <a href="http://www.dropbox.com">Dropbox</a> account.</p>

    <p>It allows you to edit your TiddlyWiki online and save the file back
    into your dropbox account</p>
    
    <% String wikiBase = com.horsefire.tiddly.tiddlybox.BootstrapListener.WIKI_URL; %>
    <p>Use the url to specify where you wiki is. If your wiki is in your Dropbox under /MyWiki/Tiddly.html, then
    go to <a href="<%= wikiBase %>/MyWiki/Tiddly.html"><%= wikiBase %>/MyWiki/Tiddly.html</a>
  </body>
</html>
