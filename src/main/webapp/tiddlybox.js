tiddlybox_setup = function() {
  readOnly = false;

  /* This redefines saveChanges from http://svn.tiddlywiki.org/Trunk/core/js/Saving.js */
  saveChanges = function(onlyIfDirty, tiddlers) {
    if(onlyIfDirty && !store.isDirty()) { return; }
    clearMessage();
    var startTime = new Date();
    jQuery.post(tiddlybox_post_url, store.allTiddlersAsHtml(),
      function() {
        displayMessage("Saved changes - " + (new Date() - startTime) + "ms");
        store.setDirty(false);
      });
  }
}

if (document.location.toString().startsWith("http")) {
  window.setTimeout(tiddlybox_setup, 1000);
}