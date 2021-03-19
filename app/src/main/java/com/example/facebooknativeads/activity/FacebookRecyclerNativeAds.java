package com.example.facebooknativeads.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebooknativeads.R;
import com.example.facebooknativeads.adapter.FaceBookRecyclerAdapterInterface;
import com.example.facebooknativeads.adapter.FacebookRecyclerAdapter;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdsManager;

import java.util.ArrayList;
import java.util.List;

public class FacebookRecyclerNativeAds extends AppCompatActivity implements FaceBookRecyclerAdapterInterface, NativeAdsManager.Listener {
    private ArrayList<String> mPostItemList;
    private NativeAdsManager mNativeAdsManager;
    private RecyclerView recyclerView;
    private List<NativeAd> mAdItems = new ArrayList<>();

//    private static fields
    private static final int AD_DISPLAY_FREQUENCY = 5;
    private static final int NO_OF_AD_REQUEST = 4;
    private static final int POST_TYPE = 0;
    private static final int AD_TYPE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_recycler_native_ads);

        // Create some dummy post items
        mPostItemList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            mPostItemList.add("RecyclerView Item #" + i);
        }

        mNativeAdsManager = new NativeAdsManager(this, getResources().getString(R.string.facebook_native_placement_id), NO_OF_AD_REQUEST);
        mNativeAdsManager.loadAds();
        mNativeAdsManager.setListener(this);

        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    public void onAdsLoaded() {
        if(getApplicationContext() == null){
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FacebookRecyclerAdapter(this));
    }

    @Override
    public void onAdError(AdError adError) {
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(this, "Error: "+adError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AD_TYPE) {
            NativeAdLayout inflatedView =
                    (NativeAdLayout)
                            LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.facebook_native_layout, parent, false);
            return new AdHolder(inflatedView);
        } else {
            View inflatedView =
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recycler_post_item, parent, false);
            return new PostHolder(inflatedView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == AD_TYPE) {
            NativeAd ad;
            if (mAdItems.size() > position / AD_DISPLAY_FREQUENCY) {
                ad = mAdItems.get(position / AD_DISPLAY_FREQUENCY);
            } else {
                ad = mNativeAdsManager.nextNativeAd();
                if (!ad.isAdInvalidated()) {
                    mAdItems.add(ad);
                } else {

                }
            }

            AdHolder adHolder = (AdHolder) holder;
            adHolder.adChoicesContainer.removeAllViews();

            if (ad != null) {

                adHolder.tvAdTitle.setText(ad.getAdvertiserName());
                adHolder.tvAdBody.setText(ad.getAdBodyText());
                adHolder.tvAdSocialContext.setText(ad.getAdSocialContext());
                adHolder.tvAdSponsoredLabel.setText(ad.getSponsoredTranslation());
                adHolder.btnAdCallToAction.setText(ad.getAdCallToAction());
                adHolder.btnAdCallToAction.setVisibility(
                        ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                AdOptionsView adOptionsView = new AdOptionsView(this, ad, adHolder.nativeAdLayout);
                adHolder.adChoicesContainer.addView(adOptionsView, 0);

                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(adHolder.ivAdIcon);
                clickableViews.add(adHolder.mvAdMedia);
                clickableViews.add(adHolder.btnAdCallToAction);
                ad.registerViewForInteraction(
                        adHolder.nativeAdLayout, adHolder.mvAdMedia, adHolder.ivAdIcon, clickableViews);
            }
        } else {
            PostHolder postHolder = (PostHolder) holder;
            // Calculate where the next postItem index is by subtracting ads we've shown.
            int index = position - (position / AD_DISPLAY_FREQUENCY) - 1;
            postHolder.tvPostContent.setText(mPostItemList.get(index));
        }
    }

    @Override
    public int getItemCount() {
        return mPostItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position % AD_DISPLAY_FREQUENCY == 0? AD_TYPE: POST_TYPE;
    }

    //    inner viewHolder class
    private static class PostHolder extends RecyclerView.ViewHolder {
        TextView tvPostContent;
        PostHolder(View view) {
            super(view);
            tvPostContent = view.findViewById(R.id.tvPostContent);
        }
    }
    private static class AdHolder extends RecyclerView.ViewHolder {
        NativeAdLayout nativeAdLayout;
        MediaView mvAdMedia;
        MediaView ivAdIcon;
        TextView tvAdTitle;
        TextView tvAdBody;
        TextView tvAdSocialContext;
        TextView tvAdSponsoredLabel;
        Button btnAdCallToAction;
        LinearLayout adChoicesContainer;
        AdHolder(NativeAdLayout adLayout) {
            super(adLayout);
            nativeAdLayout = adLayout;
            mvAdMedia = adLayout.findViewById(R.id.native_ad_media);
            tvAdTitle = adLayout.findViewById(R.id.native_ad_title);
            tvAdBody = adLayout.findViewById(R.id.native_ad_body);
            tvAdSocialContext = adLayout.findViewById(R.id.native_ad_social_context);
            tvAdSponsoredLabel = adLayout.findViewById(R.id.native_ad_sponsored_label);
            btnAdCallToAction = adLayout.findViewById(R.id.native_ad_call_to_action);
            ivAdIcon = adLayout.findViewById(R.id.native_ad_icon);
            adChoicesContainer = adLayout.findViewById(R.id.ad_choices_container);
        }
    }

}