<%@page contentType="text/javascript" %>
tiddlybox_setup = function() {
  readOnly = false;

  /* This redefines saveChanges from http://svn.tiddlywiki.org/Trunk/core/js/Saving.js */
  saveChanges = function(onlyIfDirty, tiddlers) {
    if(onlyIfDirty && !store.isDirty())
      return;
    clearMessage();
    var startTime = new Date();
    jQuery.post('<%= com.horsefire.tiddly.tiddlybox.UserPreferences.get(request).getFullWikiPath()
%>', store.allTiddlersAsHtml(),
      function() {
        displayMessage("Saved changes - " + (new Date() - startTime) + "ms");
        store.setDirty(false);
      });
  }
}

tiddlybox_baseUrl = "<%= com.horsefire.tiddly.tiddlybox.TiddlyBoxUrls.BASE_URL %>";
if (document.location.toString().startsWith(tiddlybox_baseUrl)) {
  window.setTimeout(tiddlybox_setup, 1000);
}