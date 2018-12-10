package com.example.androidlabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Objects;

public class NewsDetails extends Fragment {

    public NewsDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);

        WebView newDetailsWebView = view.findViewById(R.id.webView);
        final ProgressBar progressBar = view.findViewById(R.id.newsDetaildProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        newDetailsWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });
        newDetailsWebView.loadUrl(Objects.requireNonNull(getArguments()).getString("newsUrl"));

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
