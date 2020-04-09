package com.example.lab5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.lab5.adapter.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class lab5Activity extends AppCompatActivity {

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, lab5Activity.class);
    }


    private Adapter repoAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ProgressBar progressBar;
    private Button button;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page = 0;
    private boolean isLoading = false;
    private boolean newRequest = false;
    private Future<?> future;
    private static final String REPOS = "repos";
    private static final String SCROLL = "scroll";

    private static final String TAG = lab5Activity.class.getSimpleName();

    /**
     * Создаём пул потоков. Он необходим, чтобы переиспользовать уже созданные {@link Thread}, а не
     * создавать каждый раз новый поток при необходимости выполнения асинхронной операции.
     * Переменная статическая, т.к. должна жить вне жизненного цикла активити. Обычно такие вещи
     * инициализируются где-то в {@link android.app.Application}.
     */
    private static ExecutorService threadExecutor = Executors.newCachedThreadPool();


    private SearchTask searchTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab5_activity);
        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));

        recyclerView = findViewById(R.id.ReposList);
        progressBar = findViewById(R.id.progress);
        button = findViewById(R.id.button);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(repoAdapter = new Adapter());
        // Создаём задачку на поиск по репозиториям гитхаба. Процесс выполнения задачи будет приходить
        // в searchObserver
        searchTask = new SearchTask(searchObserver);


        recyclerView.addOnScrollListener(scrollListener);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        if(savedInstanceState!=null)
        {
            repoAdapter.setRepos(savedInstanceState.getParcelableArrayList(REPOS));
            recyclerView.scrollToPosition(savedInstanceState.getInt(SCROLL));
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(REPOS, repoAdapter.getRepos());
        outState.putInt(SCROLL, layoutManager.findFirstVisibleItemPosition());
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();//смотрим сколько элементов на экране
            int totalItemCount = layoutManager.getItemCount();//сколько всего элементов
            int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();//какая позиция первого элемента

            if (!isLoading) {//проверяем, грузим мы что-то или нет
                if ((visibleItemCount + firstVisibleItems) >= totalItemCount) {
                    isLoading = true;//ставим флаг что мы попросили еще элемены
                    doSearch();

                }
            }
        }
    };
    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (repoAdapter.getItemCount() != 0) {
                newRequest = true;
                searchTask.resetSearchParametrs(null);
                doSearch();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Обязательно необходимо отписать обсервер до уничтожения Activity, иначе мы создадим утечку
        // памяти
        searchTask.unregisterObserver();
    }

    private Observer<List<Repo>> searchObserver = new Observer<List<Repo>>() {
        @Override
        public void onLoading(@NonNull Task<List<Repo>> task) {
            progressBar.setVisibility(View.VISIBLE);
            button.setVisibility(View.INVISIBLE);
            Log.d(TAG, "onLoading");
        }

        @Override
        public void onSuccess(@NonNull Task<List<Repo>> task, @Nullable List<Repo> data) {
            Log.d(TAG, "onSuccess");
            isLoading = false;
            progressBar.setVisibility(View.INVISIBLE);
            if (!newRequest) {
                repoAdapter.addRepos(data);
            } else repoAdapter.setRepos(data);
            repoAdapter.notifyDataSetChanged();
            newRequest = false;

        }

        @Override
        public void onError(@NonNull Task<List<Repo>> task, @NonNull Exception e) {
            Log.e(TAG, "onError", e);
            progressBar.setVisibility(View.INVISIBLE);
            button.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lab5_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_repos);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (future != null) future.cancel(true);
                newRequest = true;
                if (newText.length() > 2) {
                    searchTask.resetSearchParametrs(newText);
                    doSearch();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void doSearch() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                future = threadExecutor.submit(searchTask);
            }
        }, 500);

    }
}
