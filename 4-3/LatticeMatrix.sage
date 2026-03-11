N = 23
p = 3
q = 31

R = ZZ['x']
x = R.gen()

# Public key h
h_poly = R(-15*x^22 - 15*x^21 - 12*x^20 - 14*x^19 + 4*x^18 + 11*x^17
           - 9*x^16 + 12*x^15 - 11*x^14 + 2*x^12 + 10*x^11 + 13*x^10
           - 3*x^9 + 7*x^8 - 5*x^6 + 5*x^5 + 7*x^4 - 4*x^3 + 12*x^2
           - 3*x - 12)

h_coeffs = h_poly.list()
h_coeffs += [0] * (N - len(h_coeffs))

def build_ntru_lattice(h_coeffs, N, q):
    rows = []
    for i in range(N):
        row = [0] * (2 * N)
        row[i] = 1
        for j in range(N):
            row[N + j] = int(h_coeffs[(j - i) % N]) % q
        rows.append(row)
    for i in range(N):
        row = [0] * (2 * N)
        row[N + i] = q
        rows.append(row)
    return Matrix(ZZ, rows)

M = build_ntru_lattice(h_coeffs, N, q)

with open("example02-Lattice.txt", "w") as f:
    f.write("NTRU Lattice Matrix (46x46)\n")
    f.write(f"N = {N}, p = {p}, q = {q}\n\n")
    for row in M.rows():
        f.write("[" + ", ".join(str(c) for c in row) + "]\n")

print("Written to example02-Lattice.txt")


# Read the lattice matrix from file
rows = []
with open("example02-Lattice.txt", "r") as f:
    for line in f:
        line = line.strip()
        # Only process lines that start with '['
        if line.startswith("["):
            # Strip brackets and split by comma
            entries = line.strip("[]").split(",")
            row = [int(e.strip()) for e in entries]
            rows.append(row)

# Reconstruct the matrix and run LLL
M = Matrix(ZZ, rows)
L = M.LLL()

# Write LLL-reduced matrix to file
with open("example02-LLLMatrix.txt", "w") as f:
    f.write("NTRU LLL-Reduced Matrix (46x46)\n")
    f.write(f"N = {N}, p = {p}, q = {q}\n\n")
    for row in L.rows():
        f.write("[" + ", ".join(str(c) for c in row) + "]\n")

print("LLL-reduced matrix written to example02-LLLMatrix.txt")