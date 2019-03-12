package com.example.itunelistener;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends ListActivity {
    ArrayList<String> titles;
    ArrayList<Bitmap> covers;
    ArrayList<String> previewUrls;
    iTuneListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titles = new ArrayList<>();
        covers = new ArrayList<>();
        previewUrls = new ArrayList<>();

        adapter = new iTuneListAdapter(this, titles, covers, previewUrls);
        setListAdapter(adapter);

        try {
            URL url = new URL("http://ax.phobos.apple.com.edgesuite.net/WebObjects/MZStore.woa/wpa/MRSS/topsongs/limit=20/rss.xml");

            new iTuneSAX(url, new ParserListener() {
                @Override
                public void setTitle(final String s) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            titles.add(s);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void setCover(final Bitmap b) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            covers.add(b);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void setUrl(final String element) {
                    Log.i("Preview URL", element);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            previewUrls.add(element);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Toast.makeText(this, titles.get(position), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, PreviewActivity.class);

        intent.putExtra("title", titles.get(position));
        intent.putExtra("cover", covers.get(position));
        intent.putExtra("url", previewUrls.get(position));

        startActivity(intent);
    }
}
