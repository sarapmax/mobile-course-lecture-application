package com.example.itunelistener;

import android.graphics.Bitmap;

public interface ParserListener {
    void setTitle(final String s);

    void setCover(final Bitmap bitmap);

    void setUrl (final String element);
}
