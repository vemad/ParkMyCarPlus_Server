package com.pmc.dao;

import com.pmc.model.LogPlace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import javax.transaction.Transactional;

/**
 * Created by Gaetan on 02/03/2015.
 */
@Transactional
public class LogPlaceDAOImpl implements LogPlaceDAO{
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public LogPlace saveLogPlace(LogPlace place) {
        hibernateTemplate.saveOrUpdate(place);
        return place;
    }
}
