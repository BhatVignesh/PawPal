const express = require('express');
const app = express();
const stripe = require('stripe')('sk_test_51Pbl4iEKK8V8RNsyUr0sc5dgryALrAFwHMg5SLPLyv7yeiqTpVytJzyPHd8lUElYLNJVfdBlq1vmXQxKN6HIGyuH00BMl7E5We');

// Middleware to parse JSON bodies
app.use(express.json());

// Define your payment sheet endpoint
app.post('/payment-sheet', async (req, res) => {
  try {
    const { amount } = req.body;

    // Validate the amount (you can add more validation as needed)
    if (!amount || amount <= 0) {
      return res.status(400).json({ error: 'Invalid amount' });
    }

    // Use an existing Customer ID if this is a returning customer.
    const customer = await stripe.customers.create();
    const ephemeralKey = await stripe.ephemeralKeys.create(
      { customer: customer.id },
      { apiVersion: '2024-06-20' }
    );
    const paymentIntent = await stripe.paymentIntents.create({
      amount,
      currency: 'inr', // Change currency to INR
      customer: customer.id,
      // In the latest version of the API, specifying the `automatic_payment_methods` parameter
      // is optional because Stripe enables its functionality by default.
      automatic_payment_methods: {
        enabled: true,
      },
    });

    res.json({
      paymentIntent: paymentIntent.client_secret,
      ephemeralKey: ephemeralKey.secret,
      customer: customer.id,
      publishableKey: 'pk_test_51Pbl4iEKK8V8RNsyoco4K68VzYCCUQFqvgp3XZ3XxWrot0RuZqt1LYiVl5xgl87VIt3uzJAoAiFWwMeGTyBFuhMD00Q2ja1Ixs'
    });
  } catch (err) {
    console.error('Error processing payment:', err.message);
    res.status(500).json({ error: 'Failed to process payment' });
  }
});

// Start the server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});