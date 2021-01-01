import java.util.ArrayList;
import java.util.List;

public class Friends {

    private List<Friend> friends = new ArrayList<>();

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }


    public void printFriends(Friends friends) {
        for(int i = 0; i<friends.getFriends().size(); i++) {
            System.out.println("ID: " + friends.getFriends().get(i).getTimestamp()
                             + " | Name: " + friends.getFriends().get(i).getName());
        }
    }



}
