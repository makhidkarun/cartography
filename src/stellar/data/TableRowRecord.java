package stellar.data;

public class TableRowRecord extends Record 
{
    private String code;
    private String comment;
    
    public TableRowRecord()
    {
    }
    
    public String toString()
    {
        return code + " - " + this.getValue();
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String newCode)
    {
        code = newCode;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String newComment)
    {
        comment = newComment;
    }
}