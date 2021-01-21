import static java.util.Arrays.sort;

// Klasa dla systemu przeliczania głosów wg. Hare'a i Niemeyera.
public class HareNiemeyerMethod
{
    public int[] calculateSeats(PartyToken[] partyToken, int seatsAvailable, int allVotes)
    {
        int[] seats = new int[partyToken.length + 1];
        int perOneSeat = allVotes / seatsAvailable;

        for (var party: partyToken)
        {
            party.seatsGotten += party.votesCast / perOneSeat;
            seatsAvailable -= party.votesCast / perOneSeat;
            party.votesCast = party.votesCast % perOneSeat;
        }

        sort(partyToken);
        for (int i = 0; i < seatsAvailable; ++i)
        {
            partyToken[i].seatsGotten++;
        }

        for (var party: partyToken)
        {
            seats[party.partyID] = party.seatsGotten;
        }

        return seats;
    }
}
