import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
})
export class MenuComponent {
  title = 'DARK ENGINE PROTOCOL';

  pilotos = [
    { nombre: 'ANGEL-TEDA-CANE', rango: 1, valor: '1554527952' },
    { nombre: 'michem', rango: 2, valor: '1360127407' },
    { nombre: '-SkOshM1Te-', rango: 3, valor: '1272338778' },
    { nombre: 'michem', rango: 4, valor: '1139535528' },
    { nombre: 'pajillan', rango: 5, valor: '1092074970' },
    { nombre: '23DM', rango: 6, valor: '1046363050' },
    { nombre: 'B@RB@R(KUM)', rango: 7, valor: '1009528113' },
    { nombre: '-M-E-X-I-C-A-N-O-', rango: 8, valor: '915457874' },
    { nombre: '@NEGROR@', rango: 9, valor: '910508479' },
    { nombre: '-MaqmaMexDO-', rango: 5538, valor: '128302' },
  ];
}
