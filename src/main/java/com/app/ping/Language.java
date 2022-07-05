package com.app.ping;

public enum Language {
    English("English"),
    Indian("हिन्दी"),
    Greek("Ελληνικά");

    private final String _value;
    Language(String value)
    {
        _value = value;
    }

    public String value()
    {
        return _value;
    }
}
