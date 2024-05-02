import java.util.ArrayList;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static String calc(String input) throws Exception {

        boolean arabic = false;
        boolean roman = false;

        arabic = input.matches("^([1-9]|[1-9]0) [+\\-*/] ([1-9]|[1-9]0)$");
        roman = input.matches("^.*[IVX] [+\\-*/] .*[IVX]$");

        if (!arabic && !roman) {
            throw new Exception("Некорректное выражение");
        }

        String[] exprSplit = input.split(" ");

        if(arabic) {
            switch (exprSplit[1])
            {
                case "+" -> {
                    Integer ans = Integer.parseInt(exprSplit[0]) + Integer.parseInt(exprSplit[2]);
                    return ans.toString();
                }
                case "-" -> {
                    Integer ans = Integer.parseInt(exprSplit[0]) - Integer.parseInt(exprSplit[2]);
                    return ans.toString();
                }
                case "*" -> {
                    Integer ans = Integer.parseInt(exprSplit[0]) * Integer.parseInt(exprSplit[2]);
                    return ans.toString();
                }
                case "/" -> {
                    Integer ans = Integer.parseInt(exprSplit[0]) / Integer.parseInt(exprSplit[2]);
                    return ans.toString();
                }
            }
        }

        if(roman) {
            switch (exprSplit[1]) {
                case "+" -> {
                    RomanInt ans = RomanInt.tryParse(exprSplit[0]).add(RomanInt.tryParse(exprSplit[2]));
                    return ans.toString();
                }
                case "-" -> {
                    RomanInt ans = RomanInt.tryParse(exprSplit[0]).sub(RomanInt.tryParse(exprSplit[2]));
                    return ans.toString();
                }
                case "*" -> {
                    RomanInt ans = RomanInt.tryParse(exprSplit[0]).mul(RomanInt.tryParse(exprSplit[2]));
                    return ans.toString();
                }
                case "/" -> {
                    RomanInt ans = RomanInt.tryParse(exprSplit[0]).div(RomanInt.tryParse(exprSplit[2]));
                    return ans.toString();
                }
            }

        }

        return arabic ? "арабское выражение" : "римское выражение";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите выражение!");
        String input = scanner.nextLine();

        try {
            System.out.println(calc(input));
        } catch (Exception e) {
            System.out.println("throws Exception: " + e.getMessage());
        }

    }
}

enum RomanDigit {
    I(1),
    IV(4),
    V(5),
    IX(9),
    X(10),
    XL(40),
    L(50),
    XC(90),
    C(100);

   final private int value;

    RomanDigit(int num) {
        value = num;
    };

    int getArabicValue() {
        return value;
    };

    @Override
    public String toString() {
        return switch (value) {
            case 1 -> "I";
            case 4 -> "IV";
            case 5 -> "V";
            case 9 -> "IX";
            case 10 -> "X";
            case 40 -> "XL";
            case 50 -> "L";
            case 90 -> "XC";
            case 100 -> "C";
            default -> "None";
        };
    }
}

class RomanInt {
    final int value;
    final RomanDigit[] roman_value;

    RomanInt(RomanDigit[] num){
        int buff_sum = 0;
        int end = num.length - 1;
        int curr_num;
        buff_sum += num[end].getArabicValue();

        for(int i = end-1; i >= 0; i--) {
            curr_num = num[i].getArabicValue();

            if(curr_num < num[i+1].getArabicValue()) {
                buff_sum -= curr_num;
            } else {
                buff_sum += curr_num;
            }
        }

        value = buff_sum;
        roman_value = num;
    }

    public static RomanInt tryParse(String num) throws Exception {
        ArrayList<RomanDigit> buff = new ArrayList<>();
        for(int i = 0; i < num.length(); i++) {
            switch (num.charAt(i)) {
                case 'I' -> buff.add(RomanDigit.I);
                case 'V' -> buff.add(RomanDigit.V);
                case 'X' -> buff.add(RomanDigit.X);
                case 'L' -> buff.add(RomanDigit.L);
                case 'C' -> buff.add(RomanDigit.C);
            }
        }

        return new RomanInt(buff.toArray(new RomanDigit[1]));
    }

    public static RomanInt toRoman(int num) {
        final RomanDigit[] digits = new RomanDigit[] {RomanDigit.C, RomanDigit.XC, RomanDigit.L, RomanDigit.XL, RomanDigit.X, RomanDigit.IX, RomanDigit.V, RomanDigit.IV, RomanDigit.I};
        int curr_num = num;
        ArrayList<RomanDigit> roman_num_buff = new ArrayList<>();

        for(var digit : digits) {
            while(curr_num >= digit.getArabicValue()) {
                roman_num_buff.add(digit);
                curr_num -= digit.getArabicValue();
            }
        }

        return new RomanInt(roman_num_buff.toArray(new RomanDigit[0]));
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(var item : roman_value) {
            stringBuilder.append(item.toString());
        }
        return stringBuilder.toString();
    }

    public RomanInt add(RomanInt second) {
        return RomanInt.toRoman(this.value + second.value);
    }

    public RomanInt sub(RomanInt second) throws Exception{
        int res = this.value - second.value;
        if (res < 1) {
            throw new Exception(String.format("Недопустимое значение: %d", res));
        }
        return RomanInt.toRoman(res);
    }

    public RomanInt mul(RomanInt second) {
        return RomanInt.toRoman(this.value * second.value);
    }

    public RomanInt div(RomanInt second) throws Exception {
        int res = this.value / second.value;
        if (res < 1) {
            throw new Exception(String.format("Недопустимое значение: %d", res));
        }
        return RomanInt.toRoman(res);
    }
}
