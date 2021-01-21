import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

// Wspólna nadklsa dla metody Sainte-Laguë'a oraz D'Hondta ponieważ są one prawie identyczne.
abstract public class PriorityQueueMethod 
{
    protected int[] calculateSeats(PartyToken[] partyToken, int seatsAvailable, int rate)
    {
        int[] seats = new int[partyToken.length + 1];

        for (var party: partyToken)
        {
            seats[party.partyID] = party.seatsGotten;
        }

        // Stworzenie kolejny priorytetowej z odpowiednim comperatorem.
        PriorityQueue<PartyToken> queueParties = new PriorityQueue<PartyToken>(partyToken.length, new Comparator<PartyToken>()
        {
            @Override
            public int compare(PartyToken first, PartyToken second)
            {
                return second.votesCast * (rate * first.seatsGotten + 1) - first.votesCast * (rate * second.seatsGotten + 1);
            }
        });

        Collections.addAll(queueParties, partyToken);

        while (seatsAvailable-- > 0)
        {
            var top = queueParties.remove();
            top.seatsGotten++;
            queueParties.add(top);
        }

        while (queueParties.isEmpty() == false)
        {
            var top = queueParties.remove();
            seats[top.partyID] = top.seatsGotten;
        }

        return seats;
    }
}
