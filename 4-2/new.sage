
def centered_lift(poly, q):
    coeffs = poly.list()
    new_coeffs = []
    for c in coeffs:
        c = Integer(c)
        if c > q//2:
            c -= q
        new_coeffs.append(c)
    S.<x> = PolynomialRing(ZZ)
    return S(new_coeffs)


q = 31
p = 3
halfq = q // 2
N = 23

R.<x> = PolynomialRing(GF(q), 'x')
mod_poly = x^N - 1
original_mod_poly = mod_poly

print('\n++++++++++ KEY GENERATION BEGINS ++++++++++')

fx_str = '1 + 3*(x^4 - x^3 + x^2 - x)'
fx = R(fx_str)
print('PRIVATE KEY fx = ', fx)

x1 = mod_poly
x2 = fx

t1 = R(0)
t2 = R(1)
s1 = R(1)
s2 = R(0)

while x2 != 0:
    quot, newx = x1.quo_rem(x2)
    x1 = x2
    x2 = newx

    newt = t1 - t2*quot
    news = s1 - s2*quot
    t1 = t2
    t2 = newt
    s1 = s2
    s2 = news

# x1 should now be gcd(mod_poly, fx)
print("gcd =", x1)

# inverse of gcd coefficient in GF(q)
x1inverse = x1[0]^(-1)

fqx = t1 * x1inverse
_, fqx_red = fqx.quo_rem(original_mod_poly)

print('\nfqx = ', fqx_red)
print('\nfqx mods 31 = ', centered_lift(fqx_red, q))

gx_str = 'x^9 + x^3 - x'
gx = R(gx_str)
print('\ngx = ', gx)

hx = fqx_red * gx
_, lastrem = hx.quo_rem(original_mod_poly)

print('\nPublic key h(x) mod (x^N-1) = ', lastrem)
print('Public key centered mod 31 = ', centered_lift(lastrem, q))

def centered_mod_p(poly, p):
    coeffs = poly.list()
    new_coeffs = []
    for c in coeffs:
        c = Integer(c) % p
        if c > p//2:
            c -= p
        new_coeffs.append(c)
    S.<x> = PolynomialRing(ZZ)
    return S(new_coeffs)

print('\n++++++++++ ENCRYPTION BEGINS ++++++++++')

mx_str = 'x^13 - x^11 + x^9 - 1'
mx = R(mx_str)
print('Plaintext m(x) = ', mx)

rx_str = 'x^18 + x^15 + x^7 - x^3'
rx = R(rx_str)
print('Random r(x) = ', rx)



# encryption
cx = p*rx*lastrem + mx
lastq2, lastrem2 = cx.quo_rem(original_mod_poly)
print('\nCiphertext = ', lastrem2)

# decryption
ax = fx * lastrem2
lastq3, lastrem3 = ax.quo_rem(original_mod_poly)
print('STEP 1: ax = ', lastrem3)

centered = centered_lift(lastrem3, 31)
mod3 = [c % 3 for c in centered]
print('STEP 2: coefficients mod 3 = ', mod3)

S.<x> = PolynomialRing(ZZ)
ax_centered = S(centered)
ax_mod3_centered = centered_mod_p(ax_centered, p)
print('ax mod 3 centered = ', ax_mod3_centered)