package com.project.felonydelegate;

public class User {

    String complaintId,suspicionId;

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getSuspicionId() {
        return suspicionId;
    }

    public void setSuspicionId(String suspicionId) {
        this.suspicionId = suspicionId;
    }

    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    String videoUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;
    String criminalName;

    public String getCriminalName() {
        return criminalName;
    }

    public void setCriminalName(String criminalName,String criminalMName,String criminalLName) {
        this.criminalName = criminalName+" "+criminalMName+" "+criminalLName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    String age;

    public String getUserSuspicion() {
        return userSuspicion;
    }

    public void setUserSuspicion(String userSuspicion) {
        this.userSuspicion = userSuspicion;
    }

    String userSuspicion;

    public String getUserComplaint() {
        return userComplaint;
    }

    public void setUserComplaint(String userComplaint) {
        this.userComplaint = userComplaint;
    }

    String userComplaint;
    String userAddress;

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserLocality() {
        return userLocality;
    }

    public void setUserLocality(String userLocality) {
        this.userLocality = userLocality;
    }

    public String getUserCountryName() {
        return userCountryName;
    }

    public void setUserCountryName(String userCountryName) {
        this.userCountryName = userCountryName;
    }

    String userLocality;
    String userCountryName;
    String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String fName,String mName, String lName) {
        this.userName = fName+" "+mName+" "+lName;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUserLng() {
        return userLng;
    }

    public void setUserLng(String userLng) {
        this.userLng = userLng;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String phoneCode,String phoneNo) {
        this.userPhone = phoneCode+phoneNo;
    }

    String userLat;
    String userLng;
    String userEmail;
    String userPhone;
    public String getId() {
        id= id.replace(".","");
        id= id.replace("!","");
        id= id.replace("@","");
        id= id.replace("+","");
        id= id.replace("-","");
        id= id.replace("_","");
        id= id.replace("=","");
        id= id.replace(",","");
        id= id.replace("/","");
        id= id.replace("(","");
        id= id.replace(")","");
        id= id.replace("[","");
        id= id.replace("]","");
        id= id.replace("{","");
        id= id.replace("}","");
        id= id.replace("&","");
        id= id.replace("*","");
        id= id.replace(" ","");
        id= id.replace("?","");
        id= id.replace(":","");
        id= id.replace(";","");
        id= id.replace("|","");
        id= id.replace("#","");
        id= id.replace("$","");
        id= id.replace("%","");
        id= id.replace("`","");
        id= id.replace("~","");
        id=id.replace("^","");
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
}
