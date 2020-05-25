package com.example.park_it_project_v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//Class for expandable list on FAQ page
public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> question1 = new ArrayList<String>();
        question1.add("If your vehicle or property is damaged while using Park-it, please reach out to local law enforcement for assistance. "+
                "Please contact our support team with any further questions or concerns. Our support teams contact information can be found on the 'Support/FAQ' page "+
                "located in the menu under 'About'.");

        List<String> question2 = new ArrayList<String>();
        question2.add("Your user rating is important because we want to promote an awesome experience for all our users! " +
                "If it falls too low, we can then determine what users might be going against our community guidelines." +
                " Keep an eye on it to ensure your rating is as high as possible!");

        List<String> question3 = new ArrayList<String>();
        question3.add("Park-It is not liable for any damages on your vehicle, property, or self. This is specified in the End Users Agreement presented to the user" +
                "for consent. If you would like to file a damage claim against a user which might have damaged your property, please submit a damage claim by contacting our" +
                "support team on the 'Support/FAQ' page. If you have other issues regarding damages to your property, please contact our support team as well.");

        List<String> question4 = new ArrayList<String>();
        question4.add("To contact our support team, simply navigate the the 'Support/FAQ' page located in the main menu. From here, click 'Contact Support'. "+
                "Here, you will find the contact information for our support team. We are here to help you so don't hesitate to call!");

        List<String> question5 = new ArrayList<String>();
        question5.add("A user can post a parking spot in one of two ways. The first way is by clicking the '+' button located in the bottom right "+
                "corner of the map, filling in the appropriate information for the spot and pressing 'confirm'. The alternative way to post a spot is "+
                "by navigating to the 'My Spot' page located in the menu, clicking 'add a spot', filling out the appropriate information for the spot and pressing 'confirm'.");

        List<String> question6 = new ArrayList<String>();
        question6.add("Unfortunately, you are unable to sign in without a Google verified account. If you want to create another account, " +
                "please use another Google email address!");

        List<String> question7 = new ArrayList<String>();
        question7.add("To claim a spot, simply select the spot from the map. After doing this, you will be presented with details pertaining to that spot. "+
                        "Simply, select the amount of time you wish to reserve the spot for and press 'confirm'. Your parking session will begin as soon as it has been confirmed. "+
                "While parking, you may add additional time to your session or end it before time expires. You will only be charged for the amount of time you utilized the spot. "+
                "In other words, if you end a session before time expires, you will only be charged for the time you spent parking.");

        List<String> question8 = new ArrayList<String>();
        question8.add("Unfortunately, you are unable to change your email address associated with your Park-It account through this application. However," +
                "you can change your Google email address through their platforms. If you would like to use another Google email address, please sign in with" +
                "your desired Google account.");


//Question headers for FAQ page
        expandableListDetail.put("What do I do if my vehicle / property is damaged?", question1);
        expandableListDetail.put("Why does my user rating matter?", question2);
        expandableListDetail.put("Is Park-It liable for any damages?", question3);
        expandableListDetail.put("How to contact support?", question4);
        expandableListDetail.put("How do I post a parking spot?", question5);
        expandableListDetail.put("How do I sign in without using Google?", question6);
        expandableListDetail.put("How do I claim a parking spot?", question7);
        expandableListDetail.put("Can I change the email associated with my account?", question8);

        return expandableListDetail;
    }
}