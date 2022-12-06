package org.paste.dao;

import lombok.extern.log4j.Log4j2;
import org.data.model.entity.PasteDetails;
import org.data.model.entity.PasteInfo;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Log4j2
public class PasteDao {

    @Autowired
    private Session session;

    public void save(PasteDetails pasteDetails) {
        session.save(pasteDetails);
    }

    public PasteInfo get(String shortCode, UUID userUUID) {
        Query<PasteInfo> query=session.createQuery("from PasteInfo where shortCode=:shortCode and userUUID=:userUUID");
        query.setParameter("shortCode", shortCode);
        query.setParameter("userUUID", userUUID);
        PasteInfo pasteInfo = query.uniqueResult();
        return pasteInfo;
    }

}
