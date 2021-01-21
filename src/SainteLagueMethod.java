// Klasa dla systemu przeliczania głosów wg. Sainte-Laguë'a.
public class SainteLagueMethod extends PriorityQueueMethod
{
    public int[] calculateSeats(PartyToken[] partyToken, int seatsAvailable)
    {
        return this.calculateSeats(partyToken, seatsAvailable, 2);
    }
}
