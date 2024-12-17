package com.service.main.service.admin;

import com.service.main.dto.CustomPaging;
import com.service.main.dto.CustomResult;
import com.service.main.dto.MailDto;
import com.service.main.entity.Mail;
import com.service.main.repository.MailRepository;
import com.service.main.repository.UserRepository;
import com.service.main.service.MailService;
import com.service.main.service.PagingService;
import com.service.main.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailADService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private ScheduleService scheduleService;


    @Autowired
    private PagingService pagingService;

    public CustomResult getUserMailList(){
        try{
            var users = userRepository.findAll();

            return new CustomResult(200, "Success", users);
        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }



    public CustomPaging getMailList(int pageNumber, int pageSize, String searchHeader, String status){
        try{
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

            var getMailList = mailRepository.getMailList(searchHeader, status, pageable);


            return pagingService.convertToCustomPaging(getMailList, pageNumber, pageSize);

        }catch (Exception e){
            var customPaging = new CustomPaging();
            customPaging.setStatus(400);
            customPaging.setMessage(e.getMessage());
            return customPaging;
        }
    }


    public CustomResult createNewEmail(MailDto mailDto){
        try{
            var newMail = new Mail();
            newMail.setToList(mailDto.getToList());
            newMail.setSubject(mailDto.getSubject());
            newMail.setBody(mailDto.getBody());
            newMail.setSend(mailDto.isSend());

            if(!mailDto.isSend()){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                newMail.setSendDate(LocalDateTime.parse(mailDto.getSendDate(), formatter));
                newMail.setSend(mailDto.isSend());
            }

            mailRepository.save(newMail);

            if(mailDto.isSend()){
                List<String> toList = Arrays.stream(mailDto.getToList().split(",")).toList();

                for (String to : toList){
                    mailService.sendMail(null, to, new String[]{to}, mailDto.getSubject(), mailDto.getBody());
                }
            }else{
                scheduleService.scheduleSendMail(newMail);
            }

            return new CustomResult(200, "Success", null);
        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomResult editScheduleMail(MailDto mailDto){
        try{
            var mail = mailRepository.findById(mailDto.getId());

            if(mail.isEmpty()){
                return new CustomResult(404, "Not found", null);
            }

            mail.get().setSubject(mailDto.getSubject());
            mail.get().setBody(mailDto.getBody());
            mail.get().setToList(mailDto.getToList());
            mail.get().setSend(mailDto.isSend());

            if(!mailDto.isSend()){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                mail.get().setSendDate(LocalDateTime.parse(mailDto.getSendDate(), formatter));
            }else{
                mail.get().setSendDate(LocalDateTime.now());
            }

            mailRepository.save(mail.get());

            if(mailDto.isSend()){
                List<String> toList = Arrays.stream(mailDto.getToList().split(",")).toList();

                for (String to : toList){
                    mailService.sendMail(null, to, new String[]{to}, mailDto.getSubject(), mailDto.getBody());
                }

               scheduleService.cancelMailSend(mail.get().getId());
            }else{
                scheduleService.reScheduleMail(mail.get());
            }


            return new CustomResult(200, "Success", null);


        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomResult cancelSendMail(int mailId){
        try{
            scheduleService.cancelMailSend(mailId);

            return new CustomResult(200, "Success", null);
        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }

    public CustomResult getMailById(int mailId){
        try{
            var mail = mailRepository.findById(mailId);

            if(mail.isPresent() && !mail.get().isSend()){
                return new CustomResult(200, "Success", mail.get());
            }

            return new CustomResult(400, "Not found", null);


        }catch (Exception e){
            return new CustomResult(400, e.getMessage(), null);
        }
    }
}
