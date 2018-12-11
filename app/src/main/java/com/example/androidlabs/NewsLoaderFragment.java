package com.example.androidlabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.UserManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.androidlabs.businessLogic.CacheManager;
import com.example.androidlabs.businessLogic.News;
import com.example.androidlabs.businessLogic.NewsLoader;
import com.example.androidlabs.businessLogic.UserManagementService;

import java.util.ArrayList;
import java.util.Objects;


public class NewsLoaderFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsAdapter feedAdapter;
    private ProgressBar rssReaderProgressBar;

    private CacheManager cacheManager;
    private NetworkInfo activeNetwork;
    private UserManagementService userManager;

    private OnFragmentInteractionListener mListener;

    public NewsLoaderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rss_reader, container, false);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.swipeToRefreshView);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()){
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT)
                            .show();
                    pullToRefresh.setRefreshing(false);
                } else {

                    final NewsLoader rssReader = new NewsLoader(userManager.getCurrentUser().rssNewsUrl);
                    rssReader.setOnLoadingNews(new NewsLoader.OnLoadingNews() {
                        @Override
                        public void onLoadingStart() {

                        }

                        @Override
                        public void onLoadingEnd() {
                            ArrayList<News> feedItems = rssReader.getFeedItems();
                            int j = 0;
                            for (int i = 0; i < feedItems.size(); i++){
                                if (!feedAdapter.getFeedItems().get(j).title
                                        .equals(feedItems.get(i).title)){
                                    feedAdapter.pushNews(feedItems.get(i), j);
                                    j++;
                                } else {
                                    break;
                                }
                            }

                            recyclerView.setAdapter(feedAdapter);
                            pullToRefresh.setRefreshing(false);

                        }
                    });
                    rssReader.execute();
                }
            }
        });

        userManager = UserManagementService.getInstance();
        if (userManager.getCurrentUser() == null)
            mListener.navigateToSignInDestination(R.id.rssReaderFragment);
        else {
            recyclerView = view.findViewById(R.id.recyclerView);
            rssReaderProgressBar = view.findViewById(R.id.rssReaderProgressBar);

            cacheManager = CacheManager.getInstance();
            ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetwork = connectivityManager.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

                if (userManager.getCurrentUser().rssNewsUrl == null) {
                    showChooseUrlRssDialog(view);
                } else {
                    NewsLoader rssReader = new NewsLoader(userManager.getCurrentUser().rssNewsUrl);
                    loadNewsOnline(rssReader, view);
                }
            } else {
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                loadNewsOffline(view);
            }
        }
        return view;
    }

    private void loadNewsOnline(final NewsLoader rssReader, final View view) {
        rssReader.setOnLoadingNews(new NewsLoader.OnLoadingNews() {
            @Override
            public void onLoadingStart() {
                rssReaderProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingEnd() {
                ArrayList<News> feedItems = rssReader.getFeedItems();
                ArrayList<News> feedCacheItems = cacheManager.getItemsFromCache();

                if (feedItems.size() != 0 && (feedCacheItems.size() == 0
                        || (!feedCacheItems.get(0).title.equals(feedItems.get(0).title)))) {
                    cacheManager.updateFeedItemsCache(feedItems);
                }

                feedAdapter = new NewsAdapter(getContext(), feedItems);
                feedAdapter.setOnItemCLick(new NewsAdapter.OnItemClick() {
                    @Override
                    public void navigateToDetails(NewsLoaderFragmentDirections.ActionRssReaderFragmentToNewsDetails action) {
                        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                            mListener.navigateToNewsDetails(action);
                        }
                        else {
                            Toast.makeText(
                                    getContext(),
                                    "No internet connection",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });

                if (
                    Objects.requireNonNull(getActivity()).getResources().getConfiguration().orientation == 2
                ) {
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                } else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                recyclerView.setAdapter(feedAdapter);
                rssReaderProgressBar.setVisibility(View.GONE);
            }
        });
        rssReader.execute();
    }

    private void loadNewsOffline(View view) {
        ArrayList<News> feedCacheItems = cacheManager.getItemsFromCache();
        view.findViewById(R.id.rssReaderProgressBar).setVisibility(View.VISIBLE);
        feedAdapter = new NewsAdapter(getContext(), feedCacheItems);
            feedAdapter.setOnItemCLick(new NewsAdapter.OnItemClick() {
            @Override
            public void navigateToDetails(NewsLoaderFragmentDirections.ActionRssReaderFragmentToNewsDetails action) {
                Toast.makeText(
                        getContext(),
                        "No internet connection",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
        if (Objects.requireNonNull(getActivity()).getResources().getConfiguration().orientation == 2) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        recyclerView.setAdapter(feedAdapter);
        view.findViewById(R.id.rssReaderProgressBar).setVisibility(View.GONE);
    }

    private void showChooseUrlRssDialog(final View view){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                Objects.requireNonNull(getContext())
        );
        dialogBuilder.setMessage("Please specify url for news");

        @SuppressLint("InflateParams") View rssUrlNewsPrompt = LayoutInflater.from(
                getActivity()).inflate(R.layout.rss_url_prompt, null
        );

        dialogBuilder.setView(rssUrlNewsPrompt);
        final EditText rssUrlNewsEditText = rssUrlNewsPrompt.findViewById(
                R.id.rssUrlNewsEditText
        );
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton(
                "Apply",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        userManager.updateUserRssUrl(rssUrlNewsEditText.getText().toString());
                        NewsLoader rssReader = new NewsLoader(
                                userManager.getCurrentUser().rssNewsUrl
                        );
                        loadNewsOnline(rssReader, view);
                    }
                }
        );
        dialogBuilder.setNegativeButton(
                "Default",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userManager.updateUserRssUrl(
                                "https://news.google.com/_/rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRGx1YlY4U0FtVnVHZ0pWVXlnQVAB?hl=en-US&gl=US&ceid=US:en"
                        );
                        NewsLoader rssReader = new NewsLoader(
                                userManager.getCurrentUser().rssNewsUrl
                        );
                        loadNewsOnline(rssReader, view);
                    }
                }
        );

        dialogBuilder.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void navigateToNewsDetails(
                // RssReaderFragmentDirections.ActionRssReaderFragmentToNewsDetails actionRssReaderFragmentToNewsDetails
                NewsLoaderFragmentDirections.ActionRssReaderFragmentToNewsDetails action);

        void navigateToSignInDestination(int nextDestinationId);
    }

}
