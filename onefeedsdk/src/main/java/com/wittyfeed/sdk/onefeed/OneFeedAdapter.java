package com.wittyfeed.sdk.onefeed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

/**
 * Created by aishwarydhare on 07/11/17.
 */

class OneFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "WF_SDK";
    Context activity;
    private ArrayList<Block> block_arr = new ArrayList<>();
    private WittyFeedSDKBlockFactory wittyFeedSDKBlockFactory;

    private final int SOLO_POSTER = 1;
    private final int POSTER_RV = 2;
    private final int SOLO_VIDEO = 3;
    private final int VIDEO_RV = 4;
    private final int STORY_LIST = 5;
    private final int COLLECTION_1_4 = 6;

    OneFeedAdapter(Context para_activity, ArrayList<Block> block_arr) {
        this.activity = para_activity;
        this.block_arr = block_arr;
        RequestManager requestManager = WittyGlide.with(activity);
        wittyFeedSDKBlockFactory = new WittyFeedSDKBlockFactory(requestManager, activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

        switch (viewType){
            case SOLO_POSTER:
                view = wittyFeedSDKBlockFactory.inflate_block(viewType);
                return new SoloPosterVH(view);

            case POSTER_RV:
                view = wittyFeedSDKBlockFactory.inflate_block(viewType);
                return new PosterRvVH(view);

            case SOLO_VIDEO:
                view = wittyFeedSDKBlockFactory.inflate_block(viewType);
                return new SoloVideoVH(view);

            case VIDEO_RV:
                view = wittyFeedSDKBlockFactory.inflate_block(viewType);
                return new VideoRvVH(view);

            case STORY_LIST:
                view = wittyFeedSDKBlockFactory.inflate_block(viewType);
                return new StoryListVH(view);

            case COLLECTION_1_4:
                view = wittyFeedSDKBlockFactory.inflate_block(viewType);
                return new Collection1_4VH(view);

            default:
                // TODO: 08/11/17 default view to return
                return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(holder.getAdapterPosition());
        switch (viewType){
            case SOLO_POSTER:
                SoloPosterVH soloPosterVH = (SoloPosterVH) holder;
                soloPosterVH.setUpCard(block_arr.get(holder.getAdapterPosition()));
                break;
            case POSTER_RV:
                PosterRvVH posterRvVH = (PosterRvVH) holder;
                posterRvVH.setUpCard(block_arr.get(holder.getAdapterPosition()));
                break;
            case SOLO_VIDEO:
                SoloVideoVH soloVideoVH = (SoloVideoVH) holder;
                soloVideoVH.setUpCard(block_arr.get(holder.getAdapterPosition()));
                if(holder.getAdapterPosition()+1 < block_arr.size()) {
                    if (block_arr.get(holder.getAdapterPosition() + 1).getMeta().getType().equalsIgnoreCase("video_rv")) {
                        soloVideoVH.sep_v.setVisibility(View.GONE);
                    }
                }
                break;
            case VIDEO_RV:
                VideoRvVH videoRvVH = (VideoRvVH) holder;
                videoRvVH.setUpCard(block_arr.get(holder.getAdapterPosition()));
                break;
            case STORY_LIST:
                StoryListVH storyListVH = (StoryListVH) holder;
                storyListVH.setUpCard(block_arr.get(holder.getAdapterPosition()));
                break;
            case COLLECTION_1_4:
                Collection1_4VH collection1_4VH = (Collection1_4VH) holder;
                collection1_4VH.setUpCard(block_arr.get(holder.getAdapterPosition()));
                ViewGroup.LayoutParams layoutParams = collection1_4VH.child_view.getLayoutParams();
                layoutParams.height = WittyFeedSDKSingleton.getInstance().screenWidth/2;
                collection1_4VH.child_view.setLayoutParams(layoutParams);
                Log.d(TAG, "onBindViewHolder: width:" + layoutParams.width + " height:" + layoutParams.height);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return block_arr.size();
    }


    @Override
    public int getItemViewType(int position) {
        switch (block_arr.get(position).getMeta().getType()){
            case "poster_solo": return SOLO_POSTER;
            case "poster_rv": return POSTER_RV;
            case "video_solo": return SOLO_VIDEO;
            case "video_rv":  return VIDEO_RV;
            case "story_list":  return STORY_LIST;
            case "collection_1_4":  return COLLECTION_1_4;
            default:    return 0;
        }
    }


    class Base_ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout root_rl;
        View child_view;
        View sep_v;
        ArrayList<WittyFeedSDKCardFactory> cardFactoryArrayList = new ArrayList<>();

        Base_ViewHolder(View itemView) {
            super(itemView);
            root_rl = itemView.findViewById(R.id.block_container_rl);
            sep_v = itemView.findViewById(R.id.sep_v);
        }

        void setUpCard(Block block){
            cardFactoryArrayList.clear();
            clean_card_Layouts();
        }

        void clean_card_Layouts(){
            // remove all subviews from each card if it's not equal to null
            if(root_rl != null) root_rl.removeAllViews();
        }
    }

    class SoloVideoVH extends Base_ViewHolder {
        private SoloVideoVH(View itemView) {
            super(itemView);
            root_rl =  itemView.findViewById(R.id.block_container_rl);
        }

        @Override
        void setUpCard(Block block) {
            super.setUpCard(block);
            root_rl.addView(wittyFeedSDKBlockFactory.get_constructed_block_view(block));
        }
    }

    class SoloPosterVH extends Base_ViewHolder {
        SoloPosterVH(View itemView) {
            super(itemView);
            root_rl =  itemView.findViewById(R.id.block_container_rl);
        }

        @Override
        void setUpCard(Block block) {
            super.setUpCard(block);
            root_rl.addView(wittyFeedSDKBlockFactory.get_constructed_block_view(block));
        }
    }

    class PosterRvVH extends Base_ViewHolder {
        PosterRvVH(View itemView) {
            super(itemView);
            root_rl =  itemView.findViewById(R.id.block_container_rl);
        }

        @Override
        void setUpCard(Block block) {
            super.setUpCard(block);
            root_rl.addView(wittyFeedSDKBlockFactory.get_constructed_block_view(block));
        }
    }

    class VideoRvVH extends Base_ViewHolder {
        VideoRvVH(View itemView) {
            super(itemView);
            root_rl =  itemView.findViewById(R.id.block_container_rl);
        }

        @Override
        void setUpCard(Block block) {
            super.setUpCard(block);
            root_rl.addView(wittyFeedSDKBlockFactory.get_constructed_block_view(block));
        }
    }

    class StoryListVH extends Base_ViewHolder {
        StoryListVH(View itemView) {
            super(itemView);
            root_rl =  itemView.findViewById(R.id.block_container_rl);
        }

        @Override
        void setUpCard(Block block) {
            super.setUpCard(block);
            root_rl.addView(wittyFeedSDKBlockFactory.get_constructed_block_view(block));
        }
    }

    class Collection1_4VH extends Base_ViewHolder {
        Collection1_4VH(View itemView) {
            super(itemView);
            root_rl =  itemView.findViewById(R.id.block_container_rl);
        }

        @Override
        void setUpCard(Block block) {
            super.setUpCard(block);
            child_view = wittyFeedSDKBlockFactory.get_constructed_block_view(block);
            root_rl.addView(child_view);
        }
    }


    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

//        if (holder instanceof M2F_ViewHolder) {
//            requestManager.clear(((M2F_ViewHolder) holder).left_card_rl.findViewById(R.id.cover_iv));
//            requestManager.clear(((M2F_ViewHolder) holder).right_card_rl.findViewById(R.id.cover_iv));
//        }
//        if (holder instanceof LS2_ViewHolder) {
//            requestManager.clear(((LS2_ViewHolder) holder).left_card_rl.findViewById(R.id.cover_iv));
//            requestManager.clear(((LS2_ViewHolder) holder).right_card1_rl.findViewById(R.id.cover_iv));
//            requestManager.clear(((LS2_ViewHolder) holder).right_card2_rl.findViewById(R.id.cover_iv));
//        }
//        if (holder instanceof S2L_ViewHolder) {
//            requestManager.clear(((S2L_ViewHolder) holder).right_card_rl.findViewById(R.id.cover_iv));
//            requestManager.clear(((S2L_ViewHolder) holder).left_card1_rl.findViewById(R.id.cover_iv));
//            requestManager.clear(((S2L_ViewHolder) holder).left_card2_rl.findViewById(R.id.cover_iv));
//        }
//        if (holder instanceof S3_ViewHolder) {
//            requestManager.clear(((S3_ViewHolder) holder).left_card_rl.findViewById(R.id.cover_iv));
//            requestManager.clear(((S3_ViewHolder) holder).right_card_rl.findViewById(R.id.cover_iv));
//            requestManager.clear(((S3_ViewHolder) holder).middle_card_rl.findViewById(R.id.cover_iv));
//        }
//        if (holder instanceof M2_ViewHolder) {
//            requestManager.clear(((M2_ViewHolder) holder).left_card_rl.findViewById(R.id.cover_iv));
//            requestManager.clear(((M2_ViewHolder) holder).right_card_rl.findViewById(R.id.cover_iv));
//        }

    }

}

