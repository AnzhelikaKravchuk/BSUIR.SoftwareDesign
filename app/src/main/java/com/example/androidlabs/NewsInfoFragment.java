package com.example.androidlabs;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Objects;

public class NewsInfoFragment extends Fragment {

    public NewsInfoFragment() {
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


        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
