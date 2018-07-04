package com.wittyfeed.sdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wittyfeed.sdk.onefeed.OFInterface;
import com.wittyfeed.sdk.onefeed.OFNotificationManager;
import com.wittyfeed.sdk.onefeed.OneFeedMain;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container,false);

        Button notiff_btn = view.findViewById(R.id.simulate_notiff_btn);

        notiff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_demo_fcm();
            }
        });

        Button nativeCards_btn = view.findViewById(R.id.simulate_native_cards);

        Button simulate_repeating_card_btn = view.findViewById(R.id.simulate_repeating_card);

        simulate_repeating_card_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NonRepeatingDemoActivity.class);
                startActivity(i);

            }
        });

        nativeCards_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final ViewGroup btns_ll = view.findViewById(R.id.btns_ll);
//                OFInterface ofInterface = new OFInterface() {
//                    @Override
//                    public void OnSuccess(View view) {
//                        btns_ll.addView(view);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                };
//                OneFeedMain.getInstance().ofCardFetcher.setOfInterface(ofInterface);
//                OneFeedMain.getInstance().ofCardFetcher.fetch_a_card();
                Intent i = new Intent(getActivity(), NativeCardsActivity.class);
                startActivity(i);


            }
        });


        return view;
    }

    private void send_demo_fcm() {
        int preferred_notiff_icon = R.mipmap.ic_launcher;
        Map<String, String> dummy_notiff_data = new HashMap<>();
        try {
            dummy_notiff_data.put("story_id", "60496");
            dummy_notiff_data.put("story_title", "10 Things Every Girl Should Put On Her List");
            dummy_notiff_data.put("cover_image", "https://cdn.wittyfeed.com/41441/ilik0kqmr2hpv1i4l8ya.jpeg?imwidth=960");
            dummy_notiff_data.put("story_url","https://www.wittyfeed.me/story/41441/things-every-girl-should-put-in-her-list?utm_hash=ArD51&nohead=1");

            dummy_notiff_data.put("id", "400");
            dummy_notiff_data.put("body", "Hey, Here's a new amazing story for you!");
            dummy_notiff_data.put("title", "10 Things Every Girl Should Put On Her List");
            dummy_notiff_data.put("notiff_agent", "wittyfeed_sdk");
            dummy_notiff_data.put("app_id" , "108");

            dummy_notiff_data.put("action", "" + "WittyFeedSDKContentViewActivity");

        } catch (Exception e) {
            e.printStackTrace();
        }

        OFNotificationManager
                .getInstance()
                .setHomeScreenIntent(
                        getActivity().getApplicationContext(),
                        new Intent(getActivity().getApplicationContext(), MainActivity.class)
                );

        OFNotificationManager.getInstance().handleNotification(getActivity().getApplicationContext(),"", dummy_notiff_data, preferred_notiff_icon,"108");
    }
}