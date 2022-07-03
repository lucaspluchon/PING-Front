package com.app.ping.controller;

import org.fxmisc.richtext.CodeArea;

import java.io.File;

public record FileInfo(File file, CodeArea textEditor) {
}
