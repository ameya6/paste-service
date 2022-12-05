package org.paste.dao;

import lombok.extern.log4j.Log4j2;
import org.data.model.entity.PasteDetails;
import org.data.model.entity.PasteInfo;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Log4j2
public class PasteDao {

    @Autowired
    private Session session;

    public void save(PasteDetails pasteDetails) {
        session.save(pasteDetails);
    }

}
