package net.fred.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.fred.stubs.Bank;
import net.fred.stubs.BankServiceGrpc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BankClientGrpc5 {

    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",9999)
                .usePlaintext()
                .build();

        /* Client using full stream (BIDIRECTIONAL) mode
        *A-Sync mode to create a client*
        */
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("USD")
                .setAmount(6500)
                .build();

        StreamObserver<Bank.ConvertCurrencyRequest> fullCurrencyStream = asyncStub.fullCurrencyStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("------------------------------");
                System.out.println(convertCurrencyResponse);
                System.out.println("------------------------------");
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("END");

            }
        });
        Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            int counter = 0;
            @Override
            public void run() {
                Bank.ConvertCurrencyRequest currencyRequest =  Bank.ConvertCurrencyRequest.newBuilder()
                        .setAmount((float) (Math.random()*7000))
                        .build();
                fullCurrencyStream.onNext(currencyRequest);
                System.out.println("=========> counter = "+counter);
                ++counter;
                if (counter==20){
                    fullCurrencyStream.onCompleted();
                    timer.cancel();}
            }
        }, 1000, 1000);
        System.out.println("...........?");
        System.in.read();
    }
}
