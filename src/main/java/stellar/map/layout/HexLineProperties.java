package stellar.map.layout;

public enum HexLineProperties
{
    ISLONG ("isLong"),
    ISTHREESHORT ("isThreeShort"),
    LONG_OPTION ("LongOption"),
    SHORT_OPTION1 ("shortOption1"),
    SHORT_OPTION2 ("shortOption2"),
    SHORT_OPTION3 ("shortOption3");
    
    private final String name;
    HexLineProperties (String name) { this.name = name; }
    @Override public final String toString() {return name; }
}
