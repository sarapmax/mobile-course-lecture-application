package com.example.itunelistener;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class iTuneSAX extends DefaultHandler {
    Boolean itemFound = false;
    Boolean imageFound = false;
    Boolean urlFound = false;
    ParserListener listener;
    String element = "";

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (localName.equals("item")) {
            itemFound = true;
        } else if (itemFound && localName.equals("coverArt") && attributes.getValue("height").equals("100")) {
            imageFound = true;
        } else if (itemFound && localName.equals("previewUrl")) {
            urlFound = true;
        }

        element = "";
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (itemFound && localName.equals("title")) {
            listener.setTitle(element);
        } else if (imageFound && localName.equals("coverArt")) {
            Log.i("URL", element);
            imageFound = false;

            try {
               URL url = new URL(element);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(is);

                listener.setCover(bitmap);

            } catch (Throwable t) {
                t.printStackTrace();
            }
        } else if (urlFound && localName.equals("previewUrl")) {
            listener.setUrl(element);
            urlFound = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        element += new String(ch, start, length);
    }

    public iTuneSAX(final URL url, ParserListener listener) {
        this.listener = listener;

        new Thread(new Runnable() {
            @Override
            public void run() {
                SAXParserFactory factory = SAXParserFactory.newInstance();

                try {
                    InputStream in = url.openStream();

                    SAXParser parser = factory.newSAXParser();

                    parser.parse(in, iTuneSAX.this);

                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }).start();
    }
}
