package net.fred.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.fred.stubs.Bank;
import net.fred.stubs.BankServiceGrpc;

public class BankClientGrpc1 {

    public static void main(String[] args) {
        ManagedChannel managedChannel= ManagedChannelBuilder.forAddress("localhost",9999)
                .usePlaintext()
                .build();

        /*BlockingStub only used in Unary model
        The stub gives us access to the methods
        *Sync mode to create a client*
        */
        BankServiceGrpc.BankServiceBlockingStub blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder()
                .setCurrencyFrom("MAD")
                .setCurrencyTo("USD")
                .setAmount(6500)
                .build();

        //wait until we get a response then we have a result
        Bank.ConvertCurrencyResponse currencyResponse = blockingStub.convert(request);

        System.out.println(currencyResponse);
    }
}
