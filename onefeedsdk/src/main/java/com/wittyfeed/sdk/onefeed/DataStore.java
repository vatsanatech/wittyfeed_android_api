package com.wittyfeed.sdk.onefeed;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Stores the Data-Model which is used through the app
 *
 * has 4 data model objects -
 *      1) mainFeedData: the data-model of main Feed
 *      2) searchFeedData: the data-model of feed generated by search
 *      3) searchDefaultData: the data-model which will be visible by default in the search feed
 *      4) interestDataDatum: the data-model of the fetched available interests on server
 *
 * has other helper data variables -
 *      1) mainFeedDataOffset: the offset which is currently active for main OneFeed
 *      2) lastStringSearchedL the last string which was searched in search Feed
 *
 * has several methods that perform the following operations -
 *      1) clear, reset, set, get and append in data of mainFeed
 *      2) clear, reset, set, get and append in data of searchFeed
 *      3) clear, reset, set, get and append in data of searchDefaultFeed
 *      4) clear, reset, set, get and append in data of interestDataDatum
 *      5) reset or increment mainFeed offset
 *      6) reset, set and get the lastStringSearched of searchFeed
 *
 * Read about: DataStoreManager, DataParser, Model Classes
 */

final class DataStore {

    private MainDatum mainFeedData;
    private MainDatum searchFeedData;
    private MainDatum searchDefaultData;
    private MainDatum interestsDataDatum;

    private int mainFeedDataOffset = 0;
    private String lastStringSearched;


    void clearMainFeedDataArray(){
        if(mainFeedData != null) {
            mainFeedData.getBlocks().clear();
        }
    }

    void appendInMainFeedDataArray(MainDatum datumToAppend){
        mainFeedData.getBlocks().addAll(datumToAppend.getBlocks());
    }

    List<Block> getMainFeedDataBlockArr(){
        return mainFeedData.getBlocks();
    }

    void setMainFeedData(MainDatum mainFeedData) {
        this.mainFeedData = mainFeedData;
    }

    void incrementMainFeedDataOffset(){
        this.mainFeedDataOffset += 1;
    }

    void resetMainFeedDataOffset() {
        this.mainFeedDataOffset = 0;
    }

    int getMainFeedDataOffset() {
        return mainFeedDataOffset;
    }

    String getUserIdFromConfig() {
        return mainFeedData.getConfig().getUser_id();
    }



    void clearSearchFeedDataArray(){
        if(searchFeedData != null) {
            searchFeedData.getBlocks().clear();
        }
    }

    void appendInSearchFeedDataArray(List<Block> blocksToAppend){
        searchFeedData.getBlocks().addAll(blocksToAppend);
    }

    List<Block> getSearchFeedDataBlockArr(){
        return searchFeedData.getBlocks();
    }

    void setSearchFeedData(MainDatum searchFeedData) {
        MainDatum tempDatum = new MainDatum();
        tempDatum.setConfig(searchFeedData.getConfig());
        tempDatum.setBlocks(new ArrayList<Block>());
        tempDatum.getBlocks().addAll(searchFeedData.getBlocks());
        this.searchFeedData = tempDatum;
    }

    void resetLastStringSearched() {
        this.lastStringSearched = "";
    }

    String getLastStringSearched() {
        return lastStringSearched;
    }

    void setLastStringSearched(String lastStringSearched) {
        this.lastStringSearched = lastStringSearched;
    }



    void clearSearchDefaultDataArray(){
        if(searchDefaultData != null) {
            searchDefaultData.getBlocks().clear();
        }
    }

    void appendInSearchDefaultDataArray(MainDatum datumToAppend){
        searchDefaultData.getBlocks().addAll(datumToAppend.getBlocks());
    }

    List<Block> getSearchDefaultDataBlockArr(){
        return searchDefaultData.getBlocks();
    }

    MainDatum getSearchDefaultDatum(){
        return searchDefaultData;
    }

    void setSearchDefaultData(MainDatum searchDefaultData) {
        this.searchDefaultData = searchDefaultData;
    }



    void clearInterestsDataDatum(){
        if(interestsDataDatum != null) {
            interestsDataDatum.getBlocks().clear();
        }
    }

    void appendInInterestsDataArray(MainDatum datumToAppend){
        interestsDataDatum.getBlocks().addAll(datumToAppend.getBlocks());
    }

    @Nullable
    List<Block> getInterestsDataBlockArray(){
        if(interestsDataDatum != null) {
            return interestsDataDatum.getBlocks();
        } else {
            return null;
        }
    }

    public void setInterestsDataDatum(MainDatum interestsDataDatum) {
        this.interestsDataDatum = interestsDataDatum;
    }
}