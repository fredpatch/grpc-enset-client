package net.fred.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import net.fred.stubs.Bank;
import net.fred.stubs.BankServiceGrpc;

import java.io.IOException;

public class BankClientGrpc2 {

    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",9999)
                .usePlaintext()
                .build();

        /* CLient using a-sync mode
        *A-Sync mode to create a client*
        */
        BankServiceGrpc.BankServiceStub asyncStub = BankServiceGrpc.newStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("USD")
                .setAmount(6500)
                .build();

        //second parameter stream observer calls an interface which define three methods (callback)
        asyncStub.convert(request, new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("***************************************");
                System.out.println(convertCurrencyResponse);
                System.out.println("***************************************");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("***************************************");
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("***************************************");
                System.out.println("END");
            }
        });

        //terminating the app
        System.out.println(".....?");
        System.in.read();
    }
}
