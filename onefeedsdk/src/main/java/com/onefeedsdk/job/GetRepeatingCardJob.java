package com.onefeedsdk.job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.onefeedsdk.app.Constant;
import com.onefeedsdk.app.OneFeedSdk;
import com.onefeedsdk.app.RuntimeStore;
import com.onefeedsdk.event.Event;
import com.onefeedsdk.model.FeedModel;
import com.onefeedsdk.model.RepeatingCardModel;
import com.onefeedsdk.util.LogFactory;
import com.onefeedsdk.util.Util;

import retrofit2.Call;

/**
 * Created by Yogesh Soni.
 * Company: WittyFeed
 * Date: 25-Sep-2018
 * Time: 16:47
 */
public class GetRepeatingCardJob extends BaseJob {

    //Logger
    private static LogFactory.Log log = LogFactory.getLog(GetRepeatingCardJob.class);

    private CallBackListener listener;

    private boolean isLoadMoreFeed = false;
    private int offset = 0;

    public void setListener(CallBackListener listener) {
        this.listener = listener;
    }

    public GetRepeatingCardJob(int offset) {
        super(new Params(Priority.HIGH).groupBy("home-feed"));
        this.isLoadMoreFeed = isLoadMoreFeed;
        this.offset = offset;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

        try{
            Call<RepeatingCardModel> call = OneFeedSdk.getInstance().getApiFactory().getApi()
                    .initOneFeedRepeatingCard(OneFeedSdk.getInstance().getAppId(),
                            OneFeedSdk.getInstance().getAppKey(),
                            OneFeedSdk.getInstance().getContext().getPackageName(), offset, "", 1
                            , OneFeedSdk.getInstance().getCardId(), Util.getAndroidUniqueId(), OneFeedSdk.VERSION);
            RepeatingCardModel newFeed = call.execute().body();

            RepeatingCardModel feedRepeatingCard = (RepeatingCardModel) RuntimeStore.getInstance().getValueFor(Constant.NATIVE_CARD);
            if(feedRepeatingCard != null){
                feedRepeatingCard.getRepeatingCard().getCardList().addAll(newFeed.getRepeatingCard().getCardList());
                RuntimeStore.getInstance().putKeyValues(Constant.NATIVE_CARD, feedRepeatingCard);
            }else{
                RuntimeStore.getInstance().putKeyValues(Constant.NATIVE_CARD, newFeed);
            }
            if(listener != null) {
                listener.success();
            }
        }catch (Exception e){
            log.error(e);
            if(listener != null) {
                listener.error();
            }
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if(listener != null) {
            listener.error();
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }

    public interface CallBackListener{
        void success();
        void error();
    }
}
