var editor = ace.edit("editor");
editor.getSession().setUseWorker(false);
editor.setTheme("ace/theme/solarized_dark");
editor.getSession().setMode("ace/mode/javascript");

function setText(text) {
    editor.setValue(text);
}