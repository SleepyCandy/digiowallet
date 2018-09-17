package com.digiowallet.DGOWallet.Controllers;

import com.digiowallet.DGOWallet.Entitys.accountEntity;
import com.digiowallet.DGOWallet.Entitys.datetimePOJO;
import com.digiowallet.DGOWallet.Entitys.historyEntity;
import com.digiowallet.DGOWallet.Repositorys.accountRepository;
import com.digiowallet.DGOWallet.Repositorys.histortyRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

@Component
@RestController
public class WalletController {

    static Logger log = Logger.getLogger(WalletController.class.getName());

    @Autowired
    accountRepository AccountRepository;
    @Autowired
    histortyRepository HistoryRepository;

    @PostMapping("/REGISTER")
    @ResponseBody
    private LinkedHashMap<String, Object> addAccountUser(@RequestBody accountEntity account) {
        LinkedHashMap<String, Object> json = new LinkedHashMap();
        accountEntity username = AccountRepository.findUsername(account.getUsername());
        if (username != null) {
            json.put("Result Code", "0001");
            json.put("Result Message", "invalid Username !");
            return json;
        } else {
            try {
                accountEntity accountreply = new accountEntity();
                accountreply.setUsername(account.getUsername());
                accountreply.setPassword(account.getPassword());
                accountreply.setName(account.getName());
                accountreply.setEmail(account.getEmail());
                accountreply.setMoney(account.getMoney());
                accountreply.setCreatedate(localTimeFormat());
                AccountRepository.save(accountreply);
                json.put("Result Code", "0000");
                json.put("Result Message", "SUCCESS");
                json.put("Username", account.getUsername());
                json.put("Password", account.getPassword());
                json.put("Name", account.getName());
                json.put("Email", account.getEmail());
                json.put("Addmoney", account.getMoney());
                return json;
            } catch (Exception e) {
                json.put("Result Code", "0002");
                json.put("Result Message", "invalid some element! : must have Username, password, Name, Email");
                return json;
            }
        }
    }
    @PostMapping("/ADD")
    @ResponseBody
    public LinkedHashMap<String, Object> addMoney(@RequestBody historyEntity history) {
        accountEntity accountreply = AccountRepository.findUsername(history.getUsername());
        LinkedHashMap<String, Object> json = new LinkedHashMap();
        try {
            accountreply.setMoney(history.getMoney() + accountreply.getMoney());
            AccountRepository.save(accountreply);
        }catch (NullPointerException e){
            json.put("Result Code", "0001");
            json.put("Result Message", "FALSE");
        }
        json.put("Result Code", "0000");
        json.put("Result Message", "SUCCESS");
        json.put("Username", history.getUsername());
        json.put("add money", history.getMoney());
        json.put("Total Amount", accountreply.getMoney());
        this.historySaving(history,"ADD");
        return json;
    }
    @PostMapping("/TRANFER")
    @ResponseBody
    public LinkedHashMap<String, Object>  tranferMoney(@RequestBody historyEntity history){
        accountEntity payer = AccountRepository.findUsername(history.getUsername());
        accountEntity receiver = AccountRepository.findUsername(history.getReceiver());
        LinkedHashMap<String, Object> json = new LinkedHashMap();
        try {
            if(payer.getMoney()>=history.getMoney()) {
                receiver.setMoney(receiver.getMoney() + history.getMoney());
                payer.setMoney(payer.getMoney() - history.getMoney());
                AccountRepository.save(receiver);
                AccountRepository.save(payer);
                log.info("Saved tranferMoney!");
            }else{
                json.put("Result Code", "0003");
                json.put("Result Message", "No money");
                return json;
            }
        }catch (NullPointerException e){
            json.put("Result Code", "0001");
            json.put("Result Message", "FALSE");
            return json;
        }
        json.put("Result Code", "0000");
        json.put("Result Message", "SUCCESS");
        json.put("Username", history.getUsername());
        json.put("Tranfer money", history.getMoney());
        json.put("Total Amount", payer.getMoney());
        this.historySaving(history,"TRANFER");
        return json;
    }

    @PostMapping("/WITHDRAW")
    @ResponseBody
    public LinkedHashMap<String, Object> withdrawMoney(@RequestBody historyEntity history){
        accountEntity payer = AccountRepository.findUsername(history.getUsername());
        LinkedHashMap<String, Object> json = new LinkedHashMap();
        try {
            if(payer.getMoney()>=history.getMoney()) {
                payer.setMoney(payer.getMoney() - history.getMoney());
                AccountRepository.save(payer);
                log.info("Saved Withdraw Money!");
            }else{
                json.put("Result Code", "0003");
                json.put("Result Message", "No money");
                return json;
            }
        }catch (NullPointerException e){
            json.put("Result Code", "0001");
            json.put("Result Message", "FALSE");
            return json;
        }
        json.put("Result Code", "0000");
        json.put("Result Message", "SUCCESS");
        json.put("Username", history.getUsername());
        json.put("Withdraw money", history.getMoney());
        json.put("Total Amount", payer.getMoney());
        this.historySaving(history,"WITHDRAW");
        return json;
    }
    @PostMapping("/HISTORY")
    @ResponseBody
    public LinkedHashMap<String, Object> historymoney(@RequestBody datetimePOJO datetime){
        ArrayList<HashMap<String, Object>> dataMap = new ArrayList<HashMap<String, Object>>();
        LinkedHashMap<String, Object> json = new LinkedHashMap();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");
        LocalDateTime starterDateTime = LocalDateTime.parse(datetime.getStart(), formatter);
        LocalDateTime endedDateTime = LocalDateTime.parse(datetime.getEnded(), formatter);
        List<historyEntity> historylist = HistoryRepository.findAllByDatetimeBetween(datetime.getUsername(), starterDateTime,endedDateTime);
        for(historyEntity historys : historylist){
            LinkedHashMap<String, Object> jsonhistory = new LinkedHashMap();
            String DateTime = historys.getDatetime().format(formatter);
            jsonhistory.put("date",DateTime);
            jsonhistory.put("action",historys.getAction());
            jsonhistory.put("payer",historys.getUsername());
            jsonhistory.put("receiver",historys.getReceiver());
            jsonhistory.put("money",historys.getMoney());
            dataMap.add(jsonhistory);
        }
        json.put("Result Code", "0000");
        json.put("Result Message", "SUCCESS");
        json.put("Start Date", datetime.getStart() );
        json.put("Ended Date", datetime.getEnded() );
        json.put("Username", datetime.getUsername());
        json.put("History", dataMap);
        return json;
    }

    public void historySaving(historyEntity history,String action){
        historyEntity historyreply = new historyEntity();
        historyreply.setDatetime(localTimeFormat());
        historyreply.setAction(action);
        historyreply.setUsername(history.getUsername());
        historyreply.setReceiver(history.getReceiver());
        historyreply.setMoney(history.getMoney());
        HistoryRepository.save(historyreply);
        log.info("Saved History payment");
    }

    public LocalDateTime localTimeFormat(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy HH:mm:ss");
        String formatDateTime = LocalDateTime.now().format(formatter);
        LocalDateTime DateTimeformated = LocalDateTime.parse(formatDateTime, formatter);
        return DateTimeformated;
    }
}