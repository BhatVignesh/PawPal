package com.example.pawpal10;

public class Trainee {
    private String name;
    private String abstractDetails;
    private int profilePic;

    public Trainee(String name, String abstractDetails, int profilePic) {
        this.name = name;
        this.abstractDetails = abstractDetails;
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public String getAbstractDetails() {
        return abstractDetails;
    }

    public int getProfilePic() {
        return profilePic;
    }
}

