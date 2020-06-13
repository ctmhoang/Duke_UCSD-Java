public class PhraseFilter implements Filter
{
    private final String typeOfReq; //one of three values: (“start”, ”end”, or “any”)
    private final String searchPhrase;

    public PhraseFilter(String phrase , String where)
    {
        if(!where.matches("start|end|any")) throw new IllegalArgumentException("where para only has one of these value : start ,end or any");
        typeOfReq = where;
        searchPhrase = phrase;
    }

    @Override
    public boolean satisfies(QuakeEntry qe)
    {
        String title = qe.getInfo();
        switch (typeOfReq)
        {
            case "start":
                return title.startsWith(searchPhrase);
            case "end":
                return title.endsWith(searchPhrase);
            case "any":
                return title.contains(searchPhrase);
            default:
                return false;
        }
    }


}
