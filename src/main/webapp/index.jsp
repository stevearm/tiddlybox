<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
  <title>TiddlyBox</title>
  <script type="text/javascript">

function getPath() {
  var path = document.getElementById('path').value.replace(/\\/g, '/');
  if (path[0] != '/') {
    return '/'.concat(path);
  }
  return path;
}

function updateLink() {
  var baseUrl = '<%= com.horsefire.tiddly.tiddlybox.BootstrapListener.WIKI_URL %>';
  var linkElement = document.getElementById('link');
  var path = getPath();
  path = baseUrl.concat(path);
  linkElement.href = path;
  linkElement.innerHTML = path;
}
  </script>
</head>
<body>
  <div id="intro">
    <h1>Welcome to TiddlyBox</h1>
    <p><a href="http://www.tiddlywiki.com">TiddlyWiki</a> is a file-based wiki that's designed to be opened locally and
    saved back to the same file after editing. The whole wiki is stored in a single HTML file</p>
    <p><a href="http://www.dropbox.com">Dropbox</a> is a file synchronization program that will always make sure you have
    the newest version of a file on all computers with the Dropbox client installed.</p>
    <p>Using these two things together is very handy, but for those times when you want to view or edit your TiddlyWiki from
    a computer without a Dropbox client, I built TiddlyWiki.</p>
  </div>
  <div id="use">
    <p>If you already have a wiki, please put in the path to your Wiki here. The path is relative to your Dropbox folder
    (for &lt;home&gt;/Dropbox/Wiki/TiddlyWiki.html put in /Wiki/TiddlyWiki.html). After authorizing TiddlyBox on Dropbox.com,
    your wiki will open up. Editing the wiki will save the changes back to your account, so they'll be synced to your computer.</p>
    Dropbox path <input id="path" type="text" value="/Wiki/TiddlyWiki.html" onchange="updateLink();"/>
    is viewable at <a id="link" href="/broken">/broken</a>
  </div>
  <script type="text/javascript">updateLink();</script>
</body>
</html>
