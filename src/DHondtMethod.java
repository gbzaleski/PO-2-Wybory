// Klasa dla systemu przeliczania głosów wg. D'Hondta.
public class DHondtMethod extends PriorityQueueMethod
{
    public int[] calculateSeats(PartyToken[] partyToken, int seatsAvailable)
    {
        return this.calculateSeats(partyToken, seatsAvailable, 1);
    }
}
