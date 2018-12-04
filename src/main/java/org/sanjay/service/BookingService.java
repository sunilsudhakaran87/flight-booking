package org.sanjay.service;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.sanjay.exception.CamelApplicationException;
import org.sanjay.model.BookingRequest;
import org.sanjay.model.BookingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private ProducerTemplate producerTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public BookingResponse doBooking(BookingRequest bookingRequest) throws CamelApplicationException {
        Object response = null;

        response = producerTemplate.sendBody("direct:doBooking", ExchangePattern.InOut, bookingRequest);

        if (response instanceof CamelApplicationException) {
            logger.error("We have an exception");
            String message = ((CamelApplicationException) response).getMessage();
            throw new CamelApplicationException(message);
        }


        return (BookingResponse) response;
    }
}
