package com.onefeedsdk.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.GsonBuilder;
import com.onefeedsdk.app.Constant;
import com.onefeedsdk.app.OneFeedSdk;
import com.onefeedsdk.app.RuntimeStore;
import com.onefeedsdk.event.Event;
import com.onefeedsdk.job.GetRepeatingCardJob;
import com.onefeedsdk.job.PostUserTrackingJob;
import com.onefeedsdk.listener.CallBackListener;
import com.onefeedsdk.model.FeedModel;
import com.onefeedsdk.model.RepeatingCardModel;
import com.onefeedsdk.ui.NotificationOpenActivity;

/**
 * Created by Yogesh Soni.
 * Company: WittyFeed
 * Date: 25-September-2018
 * Time: 18:26
 */
public class OneFeedNativeCard {

    private static int SHOW_CARD_ID = -1;
    private static int OFFSET_CARD = 1;
    private static boolean HIT_API = true;

    public static synchronized String showCard(final Context context, final int cardId, final View view, String reference, boolean isVerticalImage) {

        try {

            RepeatingCardModel feed = (RepeatingCardModel) RuntimeStore.getInstance().getValueFor(String.valueOf(cardId));

            // Local Storage
            String cardFeedString = OneFeedSdk.getInstance().getDefaultAppSharedPreferences().getString(String.valueOf(cardId), "");
            if (!TextUtils.isEmpty(cardFeedString) && feed == null) {
                RepeatingCardModel repeatingCardModel = new GsonBuilder().create().fromJson(cardFeedString, RepeatingCardModel.class);
                RuntimeStore.getInstance().putKeyValues(String.valueOf(cardId), repeatingCardModel);
            }

            feed = (RepeatingCardModel) RuntimeStore.getInstance().getValueFor(String.valueOf(cardId));

            if (feed != null) {
                SHOW_CARD_ID++;
                fetchNewCard(feed, cardId, context);
                if (SHOW_CARD_ID > feed.getRepeatingCard().getCardList().size() - 1) {
                    SHOW_CARD_ID = 0;
                }
                final FeedModel.Card card = feed.getRepeatingCard().getCardList().get(SHOW_CARD_ID);

                final int[] toolbarColor = {0};
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NotificationOpenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra(Constant.CARD_VIEWED, true);
                        intent.putExtra(Constant.COLOR, toolbarColor[0]);
                        intent.putExtra(Constant.TITLE, card.getStoryTitle());
                        intent.putExtra(Constant.URL, card.getStoryUrl());
                        intent.putExtra(Constant.ID, card.getStoryId());
                        context.startActivity(intent);

                        // Util.showCustomTabBrowserByCard(context, toolbarColor[0], card.getStoryTitle(), card.getStoryUrl(), card.getStoryId());
                    }
                });

                TextView titleView = view.getRootView().findViewWithTag("native_card_title");
                titleView.setText(card.getStoryTitle());

                final ImageView imageView = view.getRootView().findViewWithTag("native_card_image");
                imageView.setImageBitmap(null);
                String url = card.getCoverImage();
                if (isVerticalImage && !TextUtils.isEmpty(card.getSquareImage())) {
                    url = card.getSquareImage();
                }

                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(resource);
                                Palette.from(resource)
                                        .generate(new Palette.PaletteAsyncListener() {
                                            @Override
                                            public void onGenerated(@NonNull Palette palette) {
                                                Palette.Swatch textSwatch = palette.getVibrantSwatch();
                                                if (textSwatch == null) {
                                                    return;
                                                }
                                                toolbarColor[0] = textSwatch.getRgb();
                                                imageView.setBackgroundColor(textSwatch.getRgb());
                                            }
                                        });
                            }
                        });
                //Tracking OneFeed Card View
                OneFeedSdk.getInstance().getJobManager().addJobInBackground(
                        new PostUserTrackingJob(Constant.CARD_VIEWED, reference, card.getStoryId()));

                return card.getSheildText();
            } else {
                fetchNewCard(feed, cardId, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void fetchNewCard(RepeatingCardModel feed, final int cardId, final Context context) {
        try {
            if (feed == null && Util.checkNetworkConnection(context)) {
                OneFeedSdk.getInstance().getJobManager().addJobInBackground(new GetRepeatingCardJob(0, cardId));
            } else if (SHOW_CARD_ID > feed.getRepeatingCard().getCardList().size() - 3 && HIT_API && Util.checkNetworkConnection(context)) {
                HIT_API = false;
                GetRepeatingCardJob job = new GetRepeatingCardJob(OFFSET_CARD, cardId);
                job.setListener(new CallBackListener() {
                    @Override
                    public void success() {
                        HIT_API = true;
                    }

                    @Override
                    public void error() {
                        HIT_API = true;
                    }
                });
                OneFeedSdk.getInstance().getJobManager().addJobInBackground(job);
                OFFSET_CARD++;
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }
}
