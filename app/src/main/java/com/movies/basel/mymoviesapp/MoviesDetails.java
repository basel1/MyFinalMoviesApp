package com.movies.basel.mymoviesapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.movies.basel.mymoviesapp.Database.DbAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

public class MoviesDetails extends AppCompatActivity {
    Movie theMovie;
    AppBarLayout movieAppBar;
    Toolbar toolbar;
    RecyclerView ReviewList;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        theMovie = MySingleton.getInstance().getMovieAt(getIntent().getIntExtra("position", 1));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ReviewList = (RecyclerView) findViewById(R.id.reviews_List);
        setTitle(theMovie.getName());
        movieAppBar = (AppBarLayout) findViewById(R.id.app_bar);
        ((TextView) findViewById(R.id.over_view)).setText("Overview : " + theMovie.getOverview());
        ((TextView) findViewById(R.id.release_date)).setText("Release Date : " + theMovie.getRelease_date());
        ((TextView) findViewById(R.id.voting_average)).setText("Vote Average : " + theMovie.getVote_rating());
        new getImage().execute(theMovie.getPoster(), theMovie.getMovie_id());
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.shareTopic) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v="+MySingleton.getInstance().getTrailerList().get(0));

            this.startActivity(Intent.createChooser(shareIntent, "share via"));

        }
        if (id == R.id.addfavorite) {
            new DbAdapter(this).addNewFavourite(theMovie);
        }

        return super.onOptionsItemSelected(item);
    }


    private class getImage extends AsyncTask<String, Void, Drawable> {

        Drawable d;

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                d = drawable_from_url(params[0]);
                new MoviesApiConnection().getReviews(params[1]);
                new MoviesApiConnection().gettrailers(params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return d;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                movieAppBar.setBackground(drawable);
            }
            L.m("nnn");
            ReviewList.setLayoutManager(new MyLinearLayoutManager(getBaseContext()));
            ReviewList.setItemAnimator(new DefaultItemAnimator());
            ReviewList.setNestedScrollingEnabled(false);
            ReviewList.setAdapter(new ReviewsAdapter());
            // trailer listner
            View.OnClickListener c = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) v.getTag();
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + MySingleton.getInstance().getTrailerList().get(id - 1))));
                    L.m("http://www.youtube.com/watch?v=" + MySingleton.getInstance().getTrailerList().get(id - 1));
                }
            };
            LinearLayout section = (LinearLayout) findViewById(R.id.movieDetailsSection);
            for (int i = 1; i <= MySingleton.getInstance().getTrailerList().size(); i++) {
                Button b = new Button(getBaseContext());
                b.setText("Watch Trailer " + i);
                b.setTag(i);
                b.setOnClickListener(c);
                section.addView(b);
            }


        }
    }

    Drawable drawable_from_url(String url) throws IOException {
        Bitmap x;
        if (url.contains("files"))
            x = BitmapFactory.decodeFile(url);
        else {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.connect();
            InputStream input = connection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            x = BitmapFactory.decodeStream(input);
        }
        L.m("imaage = " + url);
        return new BitmapDrawable(Resources.getSystem(), x);
    }

}

class MyLinearLayoutManager extends android.support.v7.widget.LinearLayoutManager {

    private static boolean canMakeInsetsDirty = true;
    private static Field insetsDirtyField = null;

    private static final int CHILD_WIDTH = 0;
    private static final int CHILD_HEIGHT = 1;
    private static final int DEFAULT_CHILD_SIZE = 100;

    private final int[] childDimensions = new int[2];
    private final RecyclerView view;

    private int childSize = DEFAULT_CHILD_SIZE;
    private boolean hasChildSize;
    private int overScrollMode = ViewCompat.OVER_SCROLL_ALWAYS;
    private final Rect tmpRect = new Rect();

    @SuppressWarnings("UnusedDeclaration")
    public MyLinearLayoutManager(Context context) {
        super(context);
        this.view = null;
    }

    @SuppressWarnings("UnusedDeclaration")
    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.view = null;
    }

    @SuppressWarnings("UnusedDeclaration")
    public MyLinearLayoutManager(RecyclerView view) {
        super(view.getContext());
        this.view = view;
        this.overScrollMode = ViewCompat.getOverScrollMode(view);
    }

    @SuppressWarnings("UnusedDeclaration")
    public MyLinearLayoutManager(RecyclerView view, int orientation, boolean reverseLayout) {
        super(view.getContext(), orientation, reverseLayout);
        this.view = view;
        this.overScrollMode = ViewCompat.getOverScrollMode(view);
    }

    public void setOverScrollMode(int overScrollMode) {
        if (overScrollMode < ViewCompat.OVER_SCROLL_ALWAYS || overScrollMode > ViewCompat.OVER_SCROLL_NEVER)
            throw new IllegalArgumentException("Unknown overscroll mode: " + overScrollMode);
        if (this.view == null) throw new IllegalStateException("view == null");
        this.overScrollMode = overScrollMode;
        ViewCompat.setOverScrollMode(view, overScrollMode);
    }

    public static int makeUnspecifiedSpec() {
        return View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);

        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

        final boolean hasWidthSize = widthMode != View.MeasureSpec.UNSPECIFIED;
        final boolean hasHeightSize = heightMode != View.MeasureSpec.UNSPECIFIED;

        final boolean exactWidth = widthMode == View.MeasureSpec.EXACTLY;
        final boolean exactHeight = heightMode == View.MeasureSpec.EXACTLY;

        final int unspecified = makeUnspecifiedSpec();

        if (exactWidth && exactHeight) {
            // in case of exact calculations for both dimensions let's use default "onMeasure" implementation
            super.onMeasure(recycler, state, widthSpec, heightSpec);
            return;
        }

        final boolean vertical = getOrientation() == VERTICAL;

        initChildDimensions(widthSize, heightSize, vertical);

        int width = 0;
        int height = 0;

        // it's possible to get scrap views in recycler which are bound to old (invalid) adapter entities. This
        // happens because their invalidation happens after "onMeasure" method. As a workaround let's clear the
        // recycler now (it should not cause any performance issues while scrolling as "onMeasure" is never
        // called whiles scrolling)
        recycler.clear();

        final int stateItemCount = state.getItemCount();
        final int adapterItemCount = getItemCount();
        // adapter always contains actual data while state might contain old data (f.e. data before the animation is
        // done). As we want to measure the view with actual data we must use data from the adapter and not from  the
        // state
        for (int i = 0; i < adapterItemCount; i++) {
            if (vertical) {
                if (!hasChildSize) {
                    if (i < stateItemCount) {
                        // we should not exceed state count, otherwise we'll get IndexOutOfBoundsException. For such items
                        // we will use previously calculated dimensions
                        measureChild(recycler, i, widthSize, unspecified, childDimensions);
                    } else {
                        logMeasureWarning(i);
                    }
                }
                height += childDimensions[CHILD_HEIGHT];
                if (i == 0) {
                    width = childDimensions[CHILD_WIDTH];
                }
                if (hasHeightSize && height >= heightSize) {
                    break;
                }
            } else {
                if (!hasChildSize) {
                    if (i < stateItemCount) {
                        // we should not exceed state count, otherwise we'll get IndexOutOfBoundsException. For such items
                        // we will use previously calculated dimensions
                        measureChild(recycler, i, unspecified, heightSize, childDimensions);
                    } else {
                        logMeasureWarning(i);
                    }
                }
                width += childDimensions[CHILD_WIDTH];
                if (i == 0) {
                    height = childDimensions[CHILD_HEIGHT];
                }
                if (hasWidthSize && width >= widthSize) {
                    break;
                }
            }
        }

        if (exactWidth) {
            width = widthSize;
        } else {
            width += getPaddingLeft() + getPaddingRight();
            if (hasWidthSize) {
                width = Math.min(width, widthSize);
            }
        }

        if (exactHeight) {
            height = heightSize;
        } else {
            height += getPaddingTop() + getPaddingBottom();
            if (hasHeightSize) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);

        if (view != null && overScrollMode == ViewCompat.OVER_SCROLL_IF_CONTENT_SCROLLS) {
            final boolean fit = (vertical && (!hasHeightSize || height < heightSize))
                    || (!vertical && (!hasWidthSize || width < widthSize));

            ViewCompat.setOverScrollMode(view, fit ? ViewCompat.OVER_SCROLL_NEVER : ViewCompat.OVER_SCROLL_ALWAYS);
        }
    }

    private void logMeasureWarning(int child) {
        if (BuildConfig.DEBUG) {
            Log.w("MyLinearLayoutManager", "Can't measure child #" + child + ", previously used dimensions will be reused." +
                    "To remove this message either use #setChildSize() method or don't run RecyclerView animations");
        }
    }

    private void initChildDimensions(int width, int height, boolean vertical) {
        if (childDimensions[CHILD_WIDTH] != 0 || childDimensions[CHILD_HEIGHT] != 0) {
            // already initialized, skipping
            return;
        }
        if (vertical) {
            childDimensions[CHILD_WIDTH] = width;
            childDimensions[CHILD_HEIGHT] = childSize;
        } else {
            childDimensions[CHILD_WIDTH] = childSize;
            childDimensions[CHILD_HEIGHT] = height;
        }
    }

    @Override
    public void setOrientation(int orientation) {
        // might be called before the constructor of this class is called
        //noinspection ConstantConditions
        if (childDimensions != null) {
            if (getOrientation() != orientation) {
                childDimensions[CHILD_WIDTH] = 0;
                childDimensions[CHILD_HEIGHT] = 0;
            }
        }
        super.setOrientation(orientation);
    }

    public void clearChildSize() {
        hasChildSize = false;
        setChildSize(DEFAULT_CHILD_SIZE);
    }

    public void setChildSize(int childSize) {
        hasChildSize = true;
        if (this.childSize != childSize) {
            this.childSize = childSize;
            requestLayout();
        }
    }

    private void measureChild(RecyclerView.Recycler recycler, int position, int widthSize, int heightSize, int[] dimensions) {
        final View child;
        try {
            child = recycler.getViewForPosition(position);
        } catch (IndexOutOfBoundsException e) {
            if (BuildConfig.DEBUG) {
                Log.w("MyLinearLayoutManager", "MyLinearLayoutManager doesn't work well with animations. Consider switching them off", e);
            }
            return;
        }

        final RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) child.getLayoutParams();

        final int hPadding = getPaddingLeft() + getPaddingRight();
        final int vPadding = getPaddingTop() + getPaddingBottom();

        final int hMargin = p.leftMargin + p.rightMargin;
        final int vMargin = p.topMargin + p.bottomMargin;

        // we must make insets dirty in order calculateItemDecorationsForChild to work
        makeInsetsDirty(p);
        // this method should be called before any getXxxDecorationXxx() methods
        calculateItemDecorationsForChild(child, tmpRect);

        final int hDecoration = getRightDecorationWidth(child) + getLeftDecorationWidth(child);
        final int vDecoration = getTopDecorationHeight(child) + getBottomDecorationHeight(child);

        final int childWidthSpec = getChildMeasureSpec(widthSize, hPadding + hMargin + hDecoration, p.width, canScrollHorizontally());
        final int childHeightSpec = getChildMeasureSpec(heightSize, vPadding + vMargin + vDecoration, p.height, canScrollVertically());

        child.measure(childWidthSpec, childHeightSpec);

        dimensions[CHILD_WIDTH] = getDecoratedMeasuredWidth(child) + p.leftMargin + p.rightMargin;
        dimensions[CHILD_HEIGHT] = getDecoratedMeasuredHeight(child) + p.bottomMargin + p.topMargin;

        // as view is recycled let's not keep old measured values
        makeInsetsDirty(p);
        recycler.recycleView(child);
    }

    private static void makeInsetsDirty(RecyclerView.LayoutParams p) {
        if (!canMakeInsetsDirty) {
            return;
        }
        try {
            if (insetsDirtyField == null) {
                insetsDirtyField = RecyclerView.LayoutParams.class.getDeclaredField("mInsetsDirty");
                insetsDirtyField.setAccessible(true);
            }
            insetsDirtyField.set(p, true);
        } catch (NoSuchFieldException e) {
            onMakeInsertDirtyFailed();
        } catch (IllegalAccessException e) {
            onMakeInsertDirtyFailed();
        }
    }

    private static void onMakeInsertDirtyFailed() {
        canMakeInsetsDirty = false;
        if (BuildConfig.DEBUG) {
            Log.w("MyLinearLayoutManager", "Can't make LayoutParams insets dirty, decorations measurements might be incorrect");
        }
    }
}
