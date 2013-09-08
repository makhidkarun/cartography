package stellar.data;

import java.util.EnumMap;
import java.util.Map;

import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.UnmarshallingContext;

/**
 *This is an unused class designed to allow the JiBX mapping/unmapping of the
 * TabelRecords held in the Resources and Sector files to be loaded into a 
 * Enum HashMap based upon the {@link TableRecordKey} enum. This is not a correct
 * implementation because the original References data allows multiple keys. 
 * 
 * NOTE: This is unused.
 * NOTE: This is incomplete. 
 *
 * @author Thomas Jones-Low
 * @version $Revision: 1.2 $
 */
public class TableRecordMapper implements IMarshaller, IUnmarshaller, IAliasable
{
    private String m_uri;
    private int m_index;
    private String m_name;

    public TableRecordMapper ()
    {
        m_uri = null;
        m_index=0;
        m_name="table";
    }

    public TableRecordMapper (String uri, int index, String name)
    {
        m_uri = uri; m_index = index; m_name=name;
    }
    public boolean isExtension (int index)
    {
        return false;
    }

    public void marshal (Object obj, IMarshallingContext ctx)
    {
        
    }

    @Override
    public boolean isPresent (IUnmarshallingContext ctx)
    {
        return false;
    }
    
    @Override
    public boolean isExtension (String ext)
    {
        return false;
    }

    public Object unmarshal (Object obj, IUnmarshallingContext ictx)
        throws JiBXException
    {
        UnmarshallingContext ctx = (UnmarshallingContext)ictx;
        if (!ictx.isAt(m_uri, m_name)) {
            ctx.throwStartTagNameError(m_uri, m_name);
        }
        Map<TableRecordKey, TableRecord> map = (Map<TableRecordKey, TableRecord>)obj;
        if (map == null)
        {
            map = new EnumMap<TableRecordKey, TableRecord>(TableRecordKey.class);
        }
        
        // Here we are at the beginning of the <table> tag ->TableRecord
        TableRecord table; 
        ProviderRecord pro; 
        References ref = (References)ctx.getStackTop();
        while (ctx.isAt (m_uri, m_name))
        {
            String id = ctx.attributeText("", "id");
            String desc = ctx.attributeText ("", "desc");
            String provider = ctx.attributeText("", "provider");
            pro = ref.getProvider(provider);
            table = new TableRecord();
            table.setProvider(pro);
            table.setValue(desc);
            table.setKey (id);
            map.put (table.getTableKey(), table);
    
            ctx.parsePastStartTag(m_uri, m_name);
            while (ctx.isAt (m_uri, "record"))
            {
                Object o = ctx.unmarshalElement();
                table.add ((TableRowRecord)o);
            }
            ctx.parsePastEndTag(m_uri, m_name);
        }
        return map;
    }
}
